/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.HashSet;

/**
 *
 * @author felipelageduarte
 */
public class HTMLTags {
    
    public static final HashSet<String> tagReplace; 
    public static final HashSet<String> tagRemove; 
    
    static {
        tagReplace = new HashSet<String>();
        tagReplace.add("a");
        tagReplace.add("abbr");
        tagReplace.add("acronym");
        tagReplace.add("b");
        tagReplace.add("bdo");
        tagReplace.add("big");
        tagReplace.add("blockquote");
        tagReplace.add("center");
        tagReplace.add("del");
        tagReplace.add("details");
        tagReplace.add("dfn");
        tagReplace.add("dialog");
        tagReplace.add("em");
        tagReplace.add("h1");
        tagReplace.add("h2");
        tagReplace.add("h3");
        tagReplace.add("h4");
        tagReplace.add("h5");
        tagReplace.add("h6");
        tagReplace.add("i");
        tagReplace.add("ins");
        tagReplace.add("kbd");
        tagReplace.add("mark");
        tagReplace.add("p");
        tagReplace.add("pre");
        tagReplace.add("q");
        tagReplace.add("s");
        tagReplace.add("samp");
        tagReplace.add("small");
        tagReplace.add("span");
        tagReplace.add("strike");
        tagReplace.add("strong");
        tagReplace.add("sub");
        tagReplace.add("sup");
        tagReplace.add("table");
        tagReplace.add("tbody");
        tagReplace.add("td");
        tagReplace.add("th");
        tagReplace.add("tr");
        tagReplace.add("tt");
        tagReplace.add("time");
        tagReplace.add("u");
        tagReplace.add("wbr");
        
        
        
        
        tagRemove = new HashSet<String>();
        tagRemove.add("address");
        tagRemove.add("applet");
        tagRemove.add("area");
        tagRemove.add("aside");
        tagRemove.add("audio");
        tagRemove.add("base");
        tagRemove.add("basefont");
        tagRemove.add("bdi");
        tagRemove.add("button");
        tagRemove.add("canvas");
        tagRemove.add("caption");
        tagRemove.add("cite");
        tagRemove.add("code");
        tagRemove.add("col");
        tagRemove.add("colgroup");
        tagRemove.add("command");
        tagRemove.add("datalist");
        tagRemove.add("dd");
        tagRemove.add("dir");
        tagRemove.add("dl");
        tagRemove.add("dt");
        tagRemove.add("embed");
        tagRemove.add("fieldset");
        tagRemove.add("figcaption");
        tagRemove.add("figure");
        tagRemove.add("font");
        tagRemove.add("footer");
        tagRemove.add("form");
        tagRemove.add("frame");
        tagRemove.add("frameset");
        tagRemove.add("head");
        tagRemove.add("header");
        tagRemove.add("iframe");
        tagRemove.add("img");
        tagRemove.add("input");
        tagRemove.add("keygen");
        tagRemove.add("label");
        tagRemove.add("legend");
        tagRemove.add("li");
        tagRemove.add("link");
        tagRemove.add("map");
        tagRemove.add("menu");
        tagRemove.add("meta");
        tagRemove.add("meter");
        tagRemove.add("nav");
        tagRemove.add("noframes");
        tagRemove.add("noscript");
        tagRemove.add("object");
        tagRemove.add("ol");
        tagRemove.add("optgroup");
        tagRemove.add("option");
        tagRemove.add("output");
        tagRemove.add("param");
        tagRemove.add("progress");
        tagRemove.add("rp");
        tagRemove.add("rt");
        tagRemove.add("ruby");
        tagRemove.add("script");
        tagRemove.add("select");
        tagRemove.add("source");
        tagRemove.add("style");
        tagRemove.add("summary");
        tagRemove.add("textarea");
        tagRemove.add("tfoot");
        tagRemove.add("thead");
        tagRemove.add("title");
        tagRemove.add("track");
        tagRemove.add("ul");
        tagRemove.add("var");
        tagRemove.add("video");
        tagRemove.add("wbr");
    }
    
}
