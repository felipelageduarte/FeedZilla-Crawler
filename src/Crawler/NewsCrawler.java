/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Crawler;

import java.io.IOException;
import static utils.HTMLTags.tagRemove;
import static utils.HTMLTags.tagReplace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import utils.ProbNews;

public class NewsCrawler {

    private ArrayList<ProbNews> probNews;
    private String url;
    private boolean DEBUG = false;

    public NewsCrawler(String url) {
        this.url = url;
        probNews = new ArrayList<ProbNews>();
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

    private String processNews() {

        String news = "";

        if (probNews.size() > 1) {
            Collections.sort(probNews, new Comparator() {
                public int compare(Object o1, Object o2) {
                    ProbNews p1 = (ProbNews) o1;
                    ProbNews p2 = (ProbNews) o2;
                    return p2.news.length() - p1.news.length();
                }
            });

            double media = 0.0f;
//        double desvioPadrao = 0.0f;
            for (ProbNews pbn : probNews) {
                if (DEBUG) {
                    System.out.println("Tamanho do bloco de texto:" + pbn.news.length());
                }
                media += pbn.news.length();
            }
            media /= probNews.size();

//        for (ProbNews pbn : probNews) {
//            desvioPadrao += Math.pow(pbn.news.length() - media, 2.0);
//        }
//        desvioPadrao = Math.sqrt(desvioPadrao / (probNews.size() - 1));
//        if(DEBUG) System.out.println("Media: " + media);
//        if(DEBUG) System.out.println("Desvio Padrao: " + desvioPadrao);
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
            List<ProbNews> pn = probNews.subList(0, ++newsEnd);
            Collections.sort(pn, new Comparator() {
                public int compare(Object o1, Object o2) {
                    ProbNews p1 = (ProbNews) o1;
                    ProbNews p2 = (ProbNews) o2;
                    return p1.pos - p2.pos;
                }
            });

            for (ProbNews pbn : pn) {
                news += pbn.news + " ";
            }
        } else {
            news = probNews.get(0).news;
        }

        return news;
    }

    public String getNews() throws IOException {
        Document doc;
        try {
            doc = Jsoup.connect(this.url)
                    .timeout(60 * 1000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
        } catch (IOException ex) {
            throw new IOException("Error getting news");
        }

        removeUnneededTags(doc);
        removeBlockWithLinksAndComments(doc);
        replaceWithText(doc);
        joinTextNodes(doc);
        getBiggestsText(doc);
        return processNews();
    }

    public static void main(String[] args) throws Exception {
        String url = "";

        url = "http://www.reuters.com/article/2013/08/23/us-syria-crisis-american-idUSBRE97M08P20130823?feedType=RSS&feedName=worldNews";
        System.out.println((new NewsCrawler(url)).getNews());

        url = "http://www.bbc.co.uk/portuguese/noticias/2013/08/130807_eventos_publicos_copa_ru.shtml";
        System.out.println((new NewsCrawler(url)).getNews());

        url = "http://natelinha.ne10.uol.com.br/celebridades/2013/08/21/aos-12-anos-larissa-manoela-assume-namoro-com-colega-de-carrossel-65113.php";
        System.out.println((new NewsCrawler(url)).getNews());

        url = "http://edition.cnn.com/2013/08/22/world/meast/syria-civil-war/index.html?hpt=hp_t1";
        System.out.println((new NewsCrawler(url)).getNews());

        url = "http://www1.folha.uol.com.br/cotidiano/2013/08/1328945-nova-sinalizacao-do-transporte-coletivo-de-sp-usa-linha-dupla-como-a-das-estradas.shtml";
        System.out.println((new NewsCrawler(url)).getNews());

        url = "http://g1.globo.com/politica/noticia/2013/08/congresso-nacional-mantem-vetos-de-dilma-lei-do-ato-medico-e-ao-fpe.html";
        System.out.println((new NewsCrawler(url)).getNews());

        url = "http://www5.usp.br/31335/campus-de-piracicaba-abriga-diversidade-de-passaros-detecta-estudo/";
        System.out.println((new NewsCrawler(url)).getNews());

        url = "http://exame.abril.com.br/mercados/noticias/real-e-a-moeda-que-mais-perdeu-frente-o-dolar-no-mes";
        System.out.println((new NewsCrawler(url)).getNews());

        url = "http://www.keralanext.com/news/2013/08/23/article102.asp";
        System.out.println((new NewsCrawler(url)).getNews());

        url = "http://www.twincities.com/national/ci_23924912/banker-accused-rape-new-yorks-hamptons?source=rss";
        System.out.println((new NewsCrawler(url)).getNews());

    }
}
