package org.sis.connector.solr.cluster;

import java.util.Optional;

public interface ClusterState {

  Optional<SolrNode> findAnyAvailableNode();
}
