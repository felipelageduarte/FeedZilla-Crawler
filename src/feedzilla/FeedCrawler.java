/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feedzilla;

import Log.*;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FeedCrawler implements Runnable {

    private int count = 100;
    private int title_only = 0;
    private String url;

    public FeedCrawler(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((new Random()).nextInt(1000));
        } catch (InterruptedException ex) {
            Log.warn("Could not sleep Thread");
        }
        
        Document doc;
        try {
            doc = Jsoup.connect(this.url).get();
            Elements elements = doc.body().select("entry");
            for (Element element : elements) {
                new Feed(element);
            }
        } catch (IOException ex) {
            Log.error("Error getting feed: " + this.url, ex);
        }
    }

    public static void main(String[] args) throws Exception {
        String url = "http://api.feedzilla.com/v1/categories/27/subcategories/1370/articles.atom?count=100";
        new FeedCrawler(url).run();
    }
}
