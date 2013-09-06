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
public class Feed2 {

    public String title;
    public String id;
    public String copyright;
    public String updated;
    public String url;

    public List<Feed> entries = new ArrayList<Feed>();

    public Feed2(String url) {
        this.url = url;
    }

    public Feed2(String title, String id, String copyright, String updated) {
        this.title = title;
        this.id = id;
        this.copyright = copyright;
        this.updated = updated;
    }

    public Feed2 setTitle(String title) {
        this.title = title;
        return this;
    }

    public Feed2 setId(String id) {
        this.id = id;
        return this;
    }

    public Feed2 setCopyright(String copyright) {
        this.copyright = copyright;
        return this;
    }

    public Feed2 setUpdated(String updated) {
        this.updated = updated;
        return this;
    }

    public Feed2 setEntries(List<Feed> entries) {
        this.entries = entries;
        return this;
    }

    public Feed2 setUrl(String url) {
        this.url = url;
        return this;
    }

}
