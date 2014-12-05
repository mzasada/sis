package org.sis.ipc.events;

import com.google.common.collect.Multimap;
import org.sis.connector.solr.cluster.CollectionConfig;
import org.sis.connector.solr.cluster.SolrNode;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ClusterStatusUpdateEvent {
  private final Multimap<CollectionConfig, SolrNode> clusterState;

  public ClusterStatusUpdateEvent(Multimap<CollectionConfig, SolrNode> clusterState) {
    this.clusterState = clusterState;
  }

  public Set<String> getCollectionNames() {
    return clusterState.keySet().stream()
        .map(CollectionConfig::getCollectionName)
        .collect(toSet());
  }

  public Multimap<CollectionConfig, SolrNode> getClusterState() {
    return clusterState;
  }
}
