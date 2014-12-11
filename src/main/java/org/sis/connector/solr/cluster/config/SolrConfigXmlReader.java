package org.sis.connector.solr.cluster.config;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static java.lang.Integer.compare;
import static org.jsoup.Jsoup.parse;
import static org.jsoup.parser.Parser.xmlParser;

/**
 * Reads request handlers information from the {@code solrconfig.xml} file.
 *
 * @see HandlersInfo
 * @see <a href="http://wiki.apache.org/solr/SolrConfigXml">solrconfig.xml</a> at Apache Solr wiki
 * @since 1.0
 */
public class SolrConfigXmlReader {

  public HandlersInfo read(String solrConfigXml) {
    Document document = parse(solrConfigXml, "", xmlParser());
    String searchHandler = findLeastConfiguredSearchHandler(document);
    String updateHandler = findUpdateHandler(document);
    return new HandlersInfo(searchHandler, updateHandler);
  }

  private String findUpdateHandler(Document document) {
    return findEagerRequestHandlers(document, "solr.UpdateRequestHandler")
        .stream()
        .map(e -> e.attr("name"))
        .findFirst()
        .orElseThrow(() -> new InvalidConfigurationException("Could not find any update handler."));
  }

  private String findLeastConfiguredSearchHandler(Document document) {
    return findEagerRequestHandlers(document, "solr.SearchHandler")
        .stream()
        .sorted((e1, e2) -> compare(countConfigElements(e1), countConfigElements(e2)))
        .map(e -> e.attr("name"))
        .findFirst()
        .orElseThrow(() -> new InvalidConfigurationException("Could not find any search handler."));
  }

  private Elements findEagerRequestHandlers(Document document, String handlerClass) {
    return document
        .select(String.format("requestHandler[class=%s]", handlerClass))
        .not("requestHandler[startup=lazy]");
  }

  private int countConfigElements(Element element) {
    return element.select("lst[name=defaults] > *").size();
  }
}
