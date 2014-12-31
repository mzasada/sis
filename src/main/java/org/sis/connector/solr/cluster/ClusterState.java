package org.sis.connector.solr.cluster;

import java.util.Optional;

/**
 * Access point to a current state of the Solr cluster.
 *
 * @since 1.0
 */
public interface ClusterState {

  Optional<SolrNode> findAnyAvailableNode();

  Optional<SolrNode> findLeaderNodeForCollection(String collectionName);

  Optional<CollectionConfig> findCollectionConfigByName(String collectionName);
}
