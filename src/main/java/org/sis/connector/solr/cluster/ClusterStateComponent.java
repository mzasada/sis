package org.sis.connector.solr.cluster;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.sis.ipc.events.ClusterStatusUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.sis.connector.solr.cluster.util.ClusterTopologyUtils.findCollectionConfig;
import static org.sis.connector.solr.cluster.util.ClusterTopologyUtils.findCollectionLeader;

/**
 * In-memory representation of Solr cluster state. It contains information about all available collections,
 * cores and search handlers.
 *
 * @since 1.0
 */
@Component
public class ClusterStateComponent implements ClusterState {

  private final Multimap<CollectionConfig, SolrNode> collectionToNodeMapping = HashMultimap.create();

  @Autowired
  public ClusterStateComponent(EventBus eventBus) {
    eventBus.register(this);
  }

  @Subscribe
  public void onClusterStateUpdate(ClusterStatusUpdateEvent event) {
    collectionToNodeMapping.clear();
    collectionToNodeMapping.putAll(event.getClusterState());
  }

  @Override
  public Optional<SolrNode> findAnyAvailableNode() {
    return collectionToNodeMapping.values().stream().findAny();
  }

  @Override
  public Optional<SolrNode> findLeaderNodeForCollection(String collectionName) {
    return findCollectionLeader(collectionToNodeMapping, collectionName);
  }

  @Override
  public Optional<CollectionConfig> findCollectionConfigByName(String collectionName) {
    return findCollectionConfig(collectionToNodeMapping, collectionName);
  }
}
