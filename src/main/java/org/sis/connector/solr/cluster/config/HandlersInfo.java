package org.sis.connector.solr.cluster.config;

public class HandlersInfo {
  private final String searchHandler;
  private final String updateHandler;

  public HandlersInfo(String searchHandler, String updateHandler) {
    this.searchHandler = searchHandler;
    this.updateHandler = updateHandler;
  }

  public String getSearchHandler() {
    return searchHandler;
  }

  public String getUpdateHandler() {
    return updateHandler;
  }
}
