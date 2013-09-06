/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package feedzilla;

import news.*;
import Log.*;
import java.io.IOException;
import static utils.HTMLTags.tagRemove;
import static utils.HTMLTags.tagReplace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import utils.ProbNews;

public class FeedCrawler implements Runnable {
    
    private int count = 100;
    private int title_only = 0;
    private String url;
    
    public FeedCrawler() {
    }

    //remove blocos que contenha somente links e comentarios
    private void removeBlockWithLinksAndComments(Node node) {
        boolean onlyLink = true;
        //verifica se todos os nós filhos são somente links
        for (int i = 0; i < node.childNodes().size(); ++i) {
            Node child = node.childNode(i);
            if (child.nodeName().equals("#comment")) {
                child.remove();
                --i;
                continue;
            } else if (!child.nodeName().equals("a")) {
                onlyLink = false;
                break;
            }
        }

        for (int i = 0; i < node.childNodes().size(); ++i) {
            Node child = node.childNode(i);
            if (onlyLink) {
                child.remove();
            } else {
                removeBlockWithLinksAndComments(child);
            }
        }
    }

    private void replaceWithText(Node node) {
        for (int i = 0; i < node.childNodes().size(); ++i) {
            Node child = node.childNode(i);
            if (tagReplace.contains(child.nodeName())) {
                String content = "";
                if (child instanceof Element) {
                    content += ((Element) child).text();
                }
                child.replaceWith(new TextNode(content, child.baseUri()));
            } else {
                replaceWithText(child);
            }
        }
    }

    private void removeUnneededTags(Element doc) {
        for (Element element : doc.select("*")) {
            if (tagRemove.contains(element.tagName())) {
                element.remove();
            } else if (!element.hasText()) {
                element.remove();
            }
        }
    }

    private void printDOM(Node node, int shift) {
        for (int i = 0; i < shift; i++) {
            System.out.print(" ");
        }
        if (node instanceof TextNode) {
            String aux = ((TextNode) node).text();
            System.out.println(node.nodeName() + ": " + aux.length() + " - " + aux);
        } else {
            System.out.println(node.nodeName() + ": " + node.childNodeSize());
            for (int i = 0; i < node.childNodeSize(); ++i) {
                printDOM(node.childNode(i), shift + 3);
            }
        }
    }

    private void joinTextNodes(Node node) {
        boolean previousChildText = false;
        for (int i = 0; i < node.childNodes().size(); ++i) {
            Node child = node.childNode(i);
            if (child instanceof TextNode) {
                if (!previousChildText) {
                    previousChildText = true;
                } else {
                    TextNode previousNode = (TextNode) node.childNode(i - 1);
                    TextNode currentNode = (TextNode) child;
                    previousNode.replaceWith(new TextNode(previousNode.text() + currentNode.text(), child.baseUri()));
                    child.remove();
                    --i;
                }
            } else {
                previousChildText = false;
                joinTextNodes(child);
            }
        }
    }

    private void getBiggestsText(Node node) {
        int delta = 100;

        if (node instanceof TextNode) {
            TextNode textnode = (TextNode) node;
            if (textnode.text().length() > delta) {
                probNews.add(new ProbNews(textnode.text(), probNews.size()));
            }
        } else {
            for (int i = 0; i < node.childNodes().size(); ++i) {
                getBiggestsText(node.childNode(i));
            }
        }
    }

    private String getNews() {
        if(DEBUG) System.out.println("probNews size:" + probNews.size());
        Collections.sort(probNews, new Comparator() {
            public int compare(Object o1, Object o2) {
                ProbNews p1 = (ProbNews) o1;
                ProbNews p2 = (ProbNews) o2;
                return p2.news.length() - p1.news.length();
            }
        });

        double media = 0.0f;
        double desvioPadrao = 0.0f;
        for (ProbNews pbn : probNews) {
            if(DEBUG) System.out.println("Tamanho do bloco de texto:" + pbn.news.length());
            media += pbn.news.length();
        }
        media /= probNews.size();
        for (ProbNews pbn : probNews) {
            desvioPadrao += Math.pow(pbn.news.length() - media, 2.0);
        }
        desvioPadrao = Math.sqrt(desvioPadrao / (probNews.size() - 1));
        if(DEBUG) System.out.println("Media: " + media);
        if(DEBUG) System.out.println("Desvio Padrao: " + desvioPadrao);

        int newsEnd = 0;
        for (int i = 0; i < probNews.size(); ++i) {
            if (probNews.get(i).news.length() > media) {
                newsEnd = i;
            } else {
                break;
            }
        }
        //System.out.println("Indice do vetor que termina a noticia: "+newsEnd);

        /*Returns a view of the portion of this list 
         * between the specified fromIndex, inclusive, 
         * and toIndex, exclusive. (If fromIndex and 
         * toIndex are equal, the returned list is 
         * empty.)
         */
        List<ProbNews> pn = probNews.subList(0, newsEnd + 1);
        Collections.sort(pn, new Comparator() {
            public int compare(Object o1, Object o2) {
                ProbNews p1 = (ProbNews) o1;
                ProbNews p2 = (ProbNews) o2;
                return p1.pos - p2.pos;
            }
        });
        String news = "";
        for (ProbNews pbn : pn) {
            news += pbn.news + " ";
        }

        return news;
    }

    @Override
    public void run() {        
        Document doc;
        try {
            doc = Jsoup.connect(this.url).get();
            Elements elements = doc.body().select("entry");
            for (Element element : elements) {
                new FeedMessage(element);
            }
        } catch (IOException ex) {
            Log.error("Error getting feed: " + this.url, ex);
        }        
    }

    public static void main(String[] args) throws Exception {
        String url = "";

        url = "http://api.feedzilla.com/v1/categories/27/articles/search.atom";
        (new FeedCrawler().run();        
    }
}
