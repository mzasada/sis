package org.sis.connector.solr.cluster.config;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static java.lang.Integer.compare;
import static org.jsoup.Jsoup.parse;
import static org.jsoup.parser.Parser.xmlParser;

public class SolrConfigXmlReader {

  public HandlersInfo read(String solrConfigXml) {
    String searchHandler = findLeastConfiguredSearchHandler(solrConfigXml);

    return new HandlersInfo(searchHandler);
  }

  private String findLeastConfiguredSearchHandler(String solrConfigXml) {
    return findEagerRequestHandlers(parse(solrConfigXml, "", xmlParser()))
        .stream()
        .sorted((e1, e2) -> compare(countConfigElements(e1), countConfigElements(e2)))
        .map(e -> e.attr("name"))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(""));
  }

  private Elements findEagerRequestHandlers(Document document) {
    return document
        .select("requestHandler[class=solr.SearchHandler]")
        .not("requestHandler[startup=lazy]");
  }

  private int countConfigElements(Element element) {
    return element.select("lst[name=defaults] > *").size();
  }
}
