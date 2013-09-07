/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Crawler.FeedCrawler;
import Log.Log;
import feedzilla.Category;
import feedzilla.SubCategory;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
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
public class Main {

    public static void saveXML(ArrayList<Category> categories) throws IOException {
        String xml = "<Categories>\n";
        for (Category c : categories) {
            xml += c.toXML(1);
        }
        xml += "</Categories>";

        File file = new File("./data/Categories.xml");
        file.getParentFile().mkdirs();
        FileUtils.writeStringToFile(file, xml);

    }

    public static void main(String[] args) throws InterruptedException {

        Document doc;
        Elements elements;
        Scanner in;
        String categoryURL = "http://api.feedzilla.com/v1/categories.xml";
        ArrayList<Category> categories = new ArrayList<Category>();
        ArrayList<FeedCrawler> threads = new ArrayList<FeedCrawler>();

        /*
         * Mapeia todas as categorias existentes
         */
        try {
            doc = Jsoup.connect(categoryURL)
                    .timeout(5 * 1000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
            elements = doc.body().select("category");
            for (Element element : elements) {
                categories.add(new Category(element));
                sleep(500);
            }
            Collections.sort(categories);
        } catch (IOException ex) {
            Log.fatal("Could not get Categories/subcategories", ex);
            return;
        }

        try {
            saveXML(categories);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        FeedCrawler fc = null;
        for (Category cat : categories) {
            for (SubCategory subcat : cat.getSubCategoryIterator()) {
                fc = new FeedCrawler(cat.getId(), subcat.getId());
                new Thread(fc).start();
                threads.add(fc);
            }
        }

        while (true) {
            File exit = new File("./exit");

            if (exit.exists()) {
                for (FeedCrawler fcAux : threads) {
                    fcAux.kill();
                }
            } else {
                sleep(60 * 1000);
            }            
        }
    }
}
