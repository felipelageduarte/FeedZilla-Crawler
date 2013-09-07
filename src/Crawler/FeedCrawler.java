/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Crawler;

import Log.*;
import feedzilla.Feed;
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
    private boolean die;
    private final int count = 100;
    private final int title_only = 0;
    private final String url;

    public FeedCrawler(int category, int subcategory) {
        this.category = category;
        this.subcategory = subcategory;
        this.die = false;
        this.url = "http://api.feedzilla.com/v1/categories/"
                + this.category + "/subcategories/"
                + this.subcategory + "/articles.atom?count="
                + this.count + "&title_only=" + this.title_only;
    }
    
    public void kill(){
        this.die = true;
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep((new Random()).nextInt(10 * 60 * 1000));
            } catch (InterruptedException ex) {
                Log.warn("Could not sleep Thread", ex);
            }

            Document doc;
            try {
                doc = Jsoup.connect(this.url)
                        .timeout(5000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                Elements elements = doc.body().select("entry");
                for (Element element : elements) {
                    try {
                        new Thread(new Feed(category, subcategory, element)).start();
                    } catch (Exception ex) {
                        Logger.getLogger(FeedCrawler.class.getName()).log(Level.SEVERE, null, ex);
                        Log.debug(ex.getMessage(), ex);
                    }
                }
            } catch (IOException ex) {
                Log.error("Error getting feed: " + this.url, ex);
            }
        } while (!die);
    }

    public static void main(String[] args) throws Exception {
        new FeedCrawler(27, 1370).run();
    }
}
