/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedzilla;

import Crawler.NewsCrawler;
import Log.Log;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author felipelageduarte
 */
public class Feed implements Runnable {

    private final int category;
    private final int subcategory;
    private int id;
    private String title;
    private String summary;
    private String published;
    private String updated;
    private String author;
    private String link;
    private String copyright;
    private String source_title;
    private String source_link;
    private String news;
    private File newsXMLFile;

    public Feed(int category, int subcategory, Element entry) throws Exception {
        this.category = category;
        this.subcategory = subcategory;
        parser(entry);
        if (verifyIfNewsAlredyExists()) {
            throw new Exception("News " + this.category + "/" + this.subcategory + "/" + newsXMLFile.getName() + " - Already Exists");
        }
    }

    private boolean verifyIfNewsAlredyExists() {
        newsXMLFile = new File("./data/" + category + "/" + subcategory + "/" + this.id + ".xml");

        if (newsXMLFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private void parser(Element entry) {
        boolean source = false;
        for (Element element : entry.children()) {
            switch (element.nodeName()) {
                case "id":
                    this.id = Integer.parseInt(element.text().split(":")[1]);
                    break;
                case "title":
                    if (source) {
                    this.source_title = element.text();
                } else {
                    this.title = element.text();
                }
                    break;
                case "summary":
                    this.summary = element.text().split("<br")[0];
                    break;
                case "published":
                    this.published = element.text();
                    break;
                case "updated":
                    this.updated = element.text();
                    break;
                case "author":
                    this.author = element.text();
                    break;
                case "link":
                    if (source) {
                    this.source_link = element.attr("href");
                } else {
                    this.link = element.attr("href");
                }
                    break;
                case "rights":
                    this.copyright = element.text();
                    break;
                case "source":
                    source = true;
                    break;
                default:
                    Log.debug("Unknow TAG: " + element.nodeName());
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return "Feed{" + "category=" + category + ", subcategory=" + subcategory
                + ", id=" + id + ", title=" + title + ", summary=" + summary
                + ", published=" + published + ", updated=" + updated
                + ", author=" + author + ", link=" + link + ", copyright="
                + copyright + ", source_title=" + source_title
                + ", source_link=" + source_link + ", news=" + news + '}';
    }

    public String toXML() {
        String xml = "<data>\n";
        xml += "\t<category>\n";
        xml += "\t\t<id>\n\t\t\t" + category + "\n\t\t</id>\n";
        xml += "\t\t<name>\n\t\t\t" + Category.map.get(category) + "\n\t\t</name>\n";
        xml += "\t</category>\n";
        xml += "\t<subcategory>\n";
        xml += "\t\t<id>\n\t\t\t" + subcategory + "\n\t\t</id>\n";
        xml += "\t\t<name>\n\t\t\t" + SubCategory.map.get(subcategory) + "\n\t\t</name>\n";
        xml += "\t</subcategory>\n";
        xml += "\t<id>\n\t\t" + id + "\n\t</id>\n";
        xml += "\t<title>\n\t\t" + title + "\n\t</title>\n";
        xml += "\t<summary>\n\t\t" + summary + "\n\t</summary>\n";
        xml += "\t<published>\n\t\t" + published + "\n\t</published>\n";
        xml += "\t<updated>\n\t\t" + updated + "\n\t</updated>\n";
        xml += "\t<author>\n\t\t" + author + "\n\t</author>\n";
        xml += "\t<link>\n\t\t" + link + "\n\t</link>\n";
        xml += "\t<copyright>\n\t\t" + copyright + "\n\t</copyright>\n";
        xml += "\t<source>\n";
        xml += "\t\t<title>\n\t\t\t" + source_title + "\n\t\t</title>\n";
        xml += "\t\t<link>\n\t\t\t" + source_link + "\n\t\t</link>\n";
        xml += "\t</source>\n";
        xml += "\t<news>\n\t\t" + news + "\n\t</news>\n";
        xml += "</data>\n";
        return xml;
    }

    public String getUrlInParams(String url) {
        String urlAnswer = url;
        String[] urlParts = url.split("\\?");
        if(urlParts.length > 1){
            String[] params = urlParts[1].split("&");            
            for (String param : params) {
                if (param.split("=")[0].equals("url")) {
                    String[] values = param.split("=");
                    for (int i = 1; i < values.length; ++i) {
                        urlAnswer += values[i];
                    }

                }
            }
        }
        return urlAnswer;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((new Random()).nextInt(60*1000));
        } catch (InterruptedException ex) {
            Log.warn("Could not sleep Thread", ex);
        }

        Document doc = null;
        boolean get = true;
        int trysCount = 0;
        do {
            get = true;
            try {
                doc = Jsoup.connect(this.link)
                        .timeout(60 * 1000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
            } catch (IOException ex) {
                Logger.getLogger(Feed.class.getName()).log(Level.SEVERE, null, ex);
                Log.warn("News " + this.category + "/" + this.subcategory + "/" 
                        + newsXMLFile.getName() + " - Could not get Feed page from FeedZilla", ex);
                get = false;
                if (++trysCount > 5) {
                    Log.fatal("News " + this.category + "/" + this.subcategory + "/" + newsXMLFile.getName() + " - "
                            + "Five attempts and has not yet been possible to "
                            + "retrieve the page from filezilla. Ignoring this news.");
                    return;
                }
            }
        } while (!get);

        Elements elements = doc.body().select("iframe");
        for (Element element : elements) {
            try {
                this.link = URLDecoder.decode(element.attr("src"), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Feed.class.getName()).log(Level.SEVERE, null, ex);
                Log.fatal("News " + this.category + "/" + this.subcategory + "/" + newsXMLFile.getName() + " - "
                        + "Could not get the news link from FeedZilla pages");
                return;
            }
        }
        this.link = getUrlInParams(this.link);
        try {
            this.news = (new NewsCrawler(this.link)).getNews();
        } catch(Exception ex){
            Log.fatal("News " + this.category + "/" + this.subcategory + "/" + newsXMLFile.getName() + " - "
                    + "Could not retrieve news from link " + this.link, ex);
            return;
        }

        newsXMLFile.getParentFile().mkdirs();
        try {
            FileUtils.writeStringToFile(newsXMLFile, this.toXML());
            Log.info("News " + this.category + "/" + this.subcategory + "/"
                    + newsXMLFile.getName() + " - Successfuly saved!");
            System.out.println("News " + this.category + "/" + this.subcategory + "/"
                    + newsXMLFile.getName() + " - Successfuly saved!");
        } catch (IOException ex) {
            Log.error("News " + this.category + "/" + this.subcategory + "/"
                    + newsXMLFile.getName() + " - Could not save news into file", ex);
        }
    }

}
