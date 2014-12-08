package org.sis.connector.solr.cluster.config;

public class HandlersInfo {
  private final String searchHandler;

  public HandlersInfo(String searchHandler) {
    this.searchHandler = searchHandler;
  }

  public String getSearchHandler() {
    return searchHandler;
  }
}
