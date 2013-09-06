/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crawler;

import Log.Log;
import feedzilla.Category;
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

        FileUtils.writeStringToFile(new File("Categories.xml"), xml);
    }

    public static void main(String[] args) throws InterruptedException {

        Document doc;
        Elements elements;
        Scanner in;
        String categoryURL = "http://api.feedzilla.com/v1/categories.xml";
        ArrayList<Category> categories = new ArrayList<Category>();

        /*
         * Mapeia todas as categorias existentes
         */
        String categoryContent = "";
        try {
            doc = Jsoup.connect(categoryURL).get();
            elements = doc.body().select("category");
            for (Element element : elements) {
                categories.add(new Category(element));
                sleep(100);
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
        
        
//        while(true){
//            for()
//        }
    }
}
