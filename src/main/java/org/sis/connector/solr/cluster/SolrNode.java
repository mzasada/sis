package org.sis.connector.solr.cluster;

public class SolrNode {

  private final String host;

  public SolrNode(String host) {
    this.host = host;
  }

  public String getHost() {
    return host;
  }
}
