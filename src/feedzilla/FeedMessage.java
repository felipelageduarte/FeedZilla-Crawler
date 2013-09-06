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
public class FeedMessage {

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

    public FeedMessage(Element element) {
        //element.
    }

    public FeedMessage setId(String id) {
        this.id = id;
        return this;
    }

    public FeedMessage setTitle(String title) {
        this.title = title;
        return this;
    }

    public FeedMessage setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public FeedMessage setPublished(String published) {
        this.published = published;
        return this;
    }

    public FeedMessage setUpdated(String updated) {
        this.updated = updated;
        return this;
    }

    public FeedMessage setAuthor(String author) {
        this.author = author;
        return this;
    }

    public FeedMessage setLink(String link) {
        this.link = link;
        return this;
    }

    public FeedMessage setCopyright(String copyright) {
        this.copyright = copyright;
        return this;
    }

    public FeedMessage setSource_title(String source_title) {
        this.source_title = source_title;
        return this;
    }

    public FeedMessage setSource_link(String source_link) {
        this.source_link = source_link;
        return this;
    }
}
