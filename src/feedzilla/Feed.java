/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedzilla;

import org.jsoup.nodes.Element;

/**
 *
 * @author felipelageduarte
 */
public class Feed {

    public int category;
    public int subcategory;
    public String id;
    public String title;
    public String summary;
    public String published;
    public String updated;
    public String author;
    public String link;
    public String copyright;
    public String source_title;
    public String source_link;

    public Feed(Element element) {
        
    }

    public Feed setId(String id) {
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
}
