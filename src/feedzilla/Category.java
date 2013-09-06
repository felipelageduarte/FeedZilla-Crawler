/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedzilla;

import java.util.ArrayList;
import java.util.Collections;
import org.jsoup.nodes.Element;

/**
 *
 * @author felipelageduarte
 */
public class Category implements Comparable<Category>{

    private int id;
    private String name;

    private ArrayList<SubCategory> subCategories;

    public Category(Element category) {
        subCategories = new ArrayList<SubCategory>();
        
        for (Element element : category.children()) {
            if ("a:category_id".equals(element.nodeName())) {
                this.id = Integer.parseInt(element.text());
            } else if ("a:english_category_name".equals(element.nodeName())) {
                this.name = element.text();
            }
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
    
    @Override
    public String toString(){
        String aux = "";
        aux += this.id + ": " + this.name + " - subcategories size: " + subCategories.size();
        for(SubCategory sc : subCategories){
            aux += "\n\t" + sc.toString();
        }
        return aux;
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
