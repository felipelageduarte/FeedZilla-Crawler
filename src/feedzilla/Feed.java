/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedzilla;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author felipelageduarte
 */
public class Feed {

    public String title;
    public String id;
    public String copyright;
    public String updated;
    public String url;

    public List<FeedMessage> entries = new ArrayList<FeedMessage>();

    public Feed(String url) {
        this.url = url;
    }

    public Feed(String title, String id, String copyright, String updated) {
        this.title = title;
        this.id = id;
        this.copyright = copyright;
        this.updated = updated;
    }

    public Feed setTitle(String title) {
        this.title = title;
        return this;
    }

    public Feed setId(String id) {
        this.id = id;
        return this;
    }

    public Feed setCopyright(String copyright) {
        this.copyright = copyright;
        return this;
    }

    public Feed setUpdated(String updated) {
        this.updated = updated;
        return this;
    }

    public Feed setEntries(List<FeedMessage> entries) {
        this.entries = entries;
        return this;
    }

    public Feed setUrl(String url) {
        this.url = url;
        return this;
    }

}
