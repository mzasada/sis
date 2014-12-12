package org.sis.connector.solr.cluster.util;

import com.google.common.collect.Multimap;
import org.sis.connector.solr.cluster.CollectionConfig;
import org.sis.connector.solr.cluster.SolrNode;

import java.util.Collection;
import java.util.Optional;

public class ClusterTopologyUtils {

  public static Optional<SolrNode> findCollectionLeader(Multimap<CollectionConfig, SolrNode> topology, String collectionName) {
    Optional<CollectionConfig> optional = topology.keySet()
        .stream()
        .filter(config -> collectionName.equals(config.getCollectionName()))
        .findFirst();
    if (optional.isPresent()) {
      return findLeaderNode(topology.get(optional.get()));
    }
    return Optional.empty();
  }

  public static Optional<SolrNode> findLeaderNode(Collection<SolrNode> nodes) {
    return nodes.stream()
        .filter(SolrNode::isLeader)
        .findFirst();
  }
}
