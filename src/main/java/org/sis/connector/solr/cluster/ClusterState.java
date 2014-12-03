package org.sis.connector.solr.cluster;

public interface ClusterState {

  SolrNode findAnyAvailableNode();
}
