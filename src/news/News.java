/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package news;

/**
 *
 * @author felipelageduarte
 */
public class News {

    private String Title;
    private String URL;
    private String categorie;
    private String subcategorie;
    private String feed;
    private String news;
    private String author;

    public News() {
    }

    public String getTitle() {
        return Title;
    }

    public News setTitle(String Title) {
        this.Title = Title;
        return this;
    }

    public String getURL() {
        return URL;
    }

    public News setURL(String URL) {
        this.URL = URL;
        return this;
    }

    public String getGrup() {
        return categorie;
    }

    public News setGrup(String grup) {
        this.categorie = grup;
        return this;
    }

    public String getSubgrup() {
        return subcategorie;
    }

    public News setSubgrup(String subgrup) {
        this.subcategorie = subgrup;
        return this;
    }

    public String getFeed() {
        return feed;
    }

    public News setFeed(String feed) {
        this.feed = feed;
        return this;
    }

    public String getNews() {
        return news;
    }

    public News setNews(String news) {
        this.news = news;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public News setAuthor(String author) {
        this.author = author;
        return this;
    }

}
