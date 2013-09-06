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

    public int category;
    public int subcategory;
    private int count = 10;
    private int title_only = 0;
    private String url;

    public FeedCrawler(int category, int subcategory) {
        this.category = category;
        this.subcategory = subcategory;
        this.url = "http://api.feedzilla.com/v1/categories/"
                + this.category + "/subcategories/"
                + this.subcategory + "/articles.atom?count="
                + this.count + "&title_only=" + this.title_only;
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
                (new Feed(category, subcategory, element)).run();
            }
        } catch (IOException ex) {
            Log.error("Error getting feed: " + this.url, ex);
        }
    }

    public static void main(String[] args) throws Exception {
        new FeedCrawler(27, 1370).run();
    }
}
