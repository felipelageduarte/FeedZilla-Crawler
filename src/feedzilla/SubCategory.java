/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedzilla;

import java.util.ArrayList;
import org.jsoup.nodes.Element;

/**
 *
 * @author felipelageduarte
 */
public class SubCategory implements Comparable<SubCategory> {

    private int category;
    private int id;
    private String name;

    public SubCategory(Element category) {
        for (Element element : category.children()) {
            if ("a:subcategory_id".equals(element.nodeName())) {
                this.id = Integer.parseInt(element.text());
            } else if ("a:english_subcategory_name".equals(element.nodeName())) {
                this.name = element.text();
            } else if ("a:category_id".equals(element.nodeName())) {
                this.category = Integer.parseInt(element.text());
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return this.category + "/" + this.id + ": " + this.name;
    }

    public String toXML(int numTab){
        String tab = "";
        for(int i=0;i<numTab;++i) tab += "\t";
        String xml = tab + "<subcategory>\n";
        xml += tab + "\t<id>" + id + "</id>\n";
        xml += tab + "\t<name>" + name + "</name>\n";        
        xml += tab + "</subcategory>\n";
        return xml;
    }
    
    public int compareTo(SubCategory o) {
        if (this.id < o.id) {
            return -1;
        }
        if (this.id > o.id) {
            return 1;
        }
        return 0;
    }
}
