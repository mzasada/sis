package org.sis.connector.solr.cluster.config;

public class SolrConfigXmlReader {

  public HandlersInfo read(String solrConfigXml) {
    System.out.println("solrConfigXml = " + solrConfigXml);
    return new HandlersInfo();
  }
}
