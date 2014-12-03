package org.sis.connector.solr.cluster;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.sis.ipc.events.ClusterStatusUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClusterStateComponent implements ClusterState {

  private final Multimap<String, SolrNode> collectionToNodeMapping = HashMultimap.create();

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
  public SolrNode findAnyAvailableNode() {
    return collectionToNodeMapping.values().stream().findAny().get();
  }
}
