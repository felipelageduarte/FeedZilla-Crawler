/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedzilla;

import Log.Log;
import java.io.IOException;
import java.util.Random;
import news.News;
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
    private News news;

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
                    if (source) this.source_title = element.text();
                    else this.title = element.text();
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
                    if (source) this.source_link = element.attr("href");
                    else this.link = element.attr("href");                    
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
        return "Feed{" + "\n\tcategory=" + category + ",\n\tsubcategory=" + subcategory
                + ",\n\tid=" + id + ",\n\ttitle=" + title + ",\n\tsummary=" + summary
                + ",\n\tpublished=" + published + ",\n\tupdated=" + updated
                + ",\n\tauthor=" + author + ",\n\tlink=" + link + ",\n\tcopyright="
                + copyright + ",\n\tsource_title=" + source_title
                + ",\n\tsource_link=" + source_link + "}\n\n";
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
        } catch (IOException ex) {
            Log.fatal("Could not get real News URL", ex);
        }

        System.out.println(this.toString());
    }
}
