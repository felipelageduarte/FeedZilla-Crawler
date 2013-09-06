/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedzilla;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author felipelageduarte
 */
public class Category implements Comparable<Category> {

    private int id;
    private String name;

    public static final HashMap<Integer, String> map;
    private ArrayList<SubCategory> subCategories;

    static{
        map = new HashMap<Integer, String>();
    }
    
    public Category(Element category) throws IOException {
        
        subCategories = new ArrayList<SubCategory>();

        for (Element element : category.children()) {
            if ("a:category_id".equals(element.nodeName())) {
                this.id = Integer.parseInt(element.text());
            } else if ("a:english_category_name".equals(element.nodeName())) {
                this.name = element.text();
            }
        }
        
        map.put(id, name);

        getSubCategory();
    }

    private void getSubCategory() throws IOException {

        String url = "http://api.feedzilla.com/v1/categories/" + id + "/subcategories.xml";
        try {
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.body().select("subcategory");
            for (Element element : elements) {
                this.subCategories.add(new SubCategory(element));
            }
        } catch (IOException ex) {
            throw new IOException(url, ex);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addSubCategory(SubCategory sc) {
        this.subCategories.add(sc);
        Collections.sort(subCategories);
    }
    
    public Iterable<SubCategory> getSubCategoryIterator(){
        return this.subCategories;
    }

    @Override
    public String toString() {
        String aux = "";
        aux += this.id + ": " + this.name + " - subcategories size: " + subCategories.size();
        for (SubCategory sc : subCategories) {
            aux += "\n\t" + sc.toString();
        }
        return aux;
    }

    public String toXML(int numTab) {
        String tab = "";
        for (int i = 0; i < numTab; ++i) {
            tab += "\t";
        }
        String xml = tab + "<category>\n";
        xml += tab + "\t<id>" + id + "</id>\n";
        xml += tab + "\t<name>" + name + "</name>\n";

        for (SubCategory sc : subCategories) {
            xml += sc.toXML(numTab + 1);
        }

        xml += tab + "</category>\n";
        return xml;
    }

    @Override
    public int compareTo(Category o) {
        if (this.id < o.id) {
            return -1;
        }
        if (this.id > o.id) {
            return 1;
        }
        return 0;
    }

}
