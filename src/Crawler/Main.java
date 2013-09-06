/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crawler;

import Log.Log;
import feedzilla.Category;
import feedzilla.FeedMessage;
import feedzilla.SubCategory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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

    public static String getContent(URL url) throws IOException {

        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                connection.getInputStream()));
        
        String inputLine, content = "";

        while ((inputLine = in.readLine()) != null) {
            content += inputLine;
        }
        in.close();
        
        return content;
    }

    public static void main(String[] args) {

        Document doc;
        Elements elements;
        Scanner in;
        URL categoryURL;
        URL subcategoryURL;

        try {
            categoryURL = new URL("http://api.feedzilla.com/v1/categories.xml");
            subcategoryURL = new URL("http://api.feedzilla.com/v1/subcategories.xml");
        } catch (MalformedURLException ex) {
            Log.fatal("Malformed URL", ex);
            return;
        }

        ArrayList<Category> categories = new ArrayList<Category>();

        /*
         * Mapeia todas as categorias existentes
         */
        String categoryContent = "";
        try {
            categoryContent = getContent(categoryURL);
        } catch (IOException ex) {
            Log.fatal("Could not get Categories", ex);
            return;
        }

        doc = Jsoup.parse(categoryContent);

        try {
            FileUtils.writeStringToFile(new File("category.xml"), categoryContent);
        } catch (IOException ex) {
            Log.fatal("Could not save category.xml", ex);
            return;
        }

        elements = doc.body().select("category");
        for (Element element : elements) {
            categories.add(new Category(element));
        }
        Collections.sort(categories);

        /*
         * Para cada categoria, mapeia todas as subcategorias existentes
         */
        try {
            in = new Scanner(subcategoryURL.openStream());
        } catch (IOException ex) {
            Log.fatal("Could not get SubCategories", ex);
            return;
        }
        String subcategoryContent = in.nextLine();
        doc = Jsoup.parse(subcategoryContent);

        try {
            FileUtils.writeStringToFile(new File("subcategory.xml"), subcategoryContent);
        } catch (IOException ex) {
            Log.fatal("Could not save category.xml", ex);
            return;
        }

        elements = doc.body().select("subcategory");
        SubCategory sc;
        for (Element element : elements) {
            sc = new SubCategory(element);
            for (Category category : categories) {
                if (category.getId() == sc.getCategory()) {
                    category.addSubCategory(sc);
                    break;
                }
            }
        }

        for (Category c : categories) {
            System.out.println(c.toString());
        }
    }
}
