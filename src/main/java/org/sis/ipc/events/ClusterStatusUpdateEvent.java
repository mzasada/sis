package org.sis.ipc.events;

import com.google.common.collect.Multimap;
import org.sis.connector.solr.cluster.SolrNode;

import java.util.Set;

public class ClusterStatusUpdateEvent {
  private final Multimap<String, SolrNode> clusterState;

  public ClusterStatusUpdateEvent(Multimap<String, SolrNode> clusterState) {
    this.clusterState = clusterState;
  }

  public Set<String> getCollectionNames() {
    return clusterState.keySet();
  }

  public Multimap<String, SolrNode> getClusterState() {
    return clusterState;
  }
}
