package org.sis.connector.solr.cluster;

import org.sis.connector.solr.cluster.config.HandlersInfo;

public class CollectionConfig {
  private final String collectionName;
  private final HandlersInfo handlersInfo;

  public CollectionConfig(String collectionName, HandlersInfo handlersInfo) {
    this.collectionName = collectionName;
    this.handlersInfo = handlersInfo;
  }

  public String getCollectionName() {
    return collectionName;
  }
}
