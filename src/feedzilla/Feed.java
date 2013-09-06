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

    private int category;
    private int subcategory;
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

    public Feed(int category, int subcategory, Element entry) {
        this.category = category;
        this.subcategory = subcategory;
        parser(entry);
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

    public Feed setCategory(int category) {
        this.category = category;
        return this;
    }

    public Feed setSubcategory(int subcategory) {
        this.subcategory = subcategory;
        return this;
    }

    public Feed setId(int id) {
        this.id = id;
        return this;
    }

    public Feed setTitle(String title) {
        this.title = title;
        return this;
    }

    public Feed setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public Feed setPublished(String published) {
        this.published = published;
        return this;
    }

    public Feed setUpdated(String updated) {
        this.updated = updated;
        return this;
    }

    public Feed setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Feed setLink(String link) {
        this.link = link;
        return this;
    }

    public Feed setCopyright(String copyright) {
        this.copyright = copyright;
        return this;
    }

    public Feed setSource_title(String source_title) {
        this.source_title = source_title;
        return this;
    }

    public Feed setSource_link(String source_link) {
        this.source_link = source_link;
        return this;
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
        
        xml += "\t<subcategory>\n\t\t" + subcategory + "\n\t</subcategory>\n";
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

    

    @Override
    public void run() {
        try {
            Thread.sleep((new Random()).nextInt(1000));
        } catch (InterruptedException ex) {
            Log.warn("Could not sleep Thread", ex);
        }

        Document doc;
        try {
            doc = Jsoup.connect(this.link).get();
            Elements elements = doc.body().select("iframe");
            for (Element element : elements) {
                this.link = element.attr("src");
            }

            this.news = (new NewsCrawler(this.link)).getNews();
        } catch (IOException ex) {
            Log.fatal("Could not get real News URL. ", ex);
        } catch (Exception ex) {
            Log.fatal("Unknow error. ", ex);
        }
                

        File file = new File("./data/"+category+"/"+subcategory+"/"+this.id+".xml");
        file.getParentFile().mkdirs();
        try {
            FileUtils.writeStringToFile(file, this.toXML());
        } catch (IOException ex) {
            Logger.getLogger(Feed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
