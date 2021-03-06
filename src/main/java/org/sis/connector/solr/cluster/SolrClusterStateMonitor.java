package org.sis.connector.solr.cluster;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.sis.ipc.events.RefreshClusterStatusEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SolrClusterStateMonitor {

  private static final String CLUSTER_STATE_ZNODE_PATH = "/clusterstate.json";
  private final NodeCache clusterStateNode;
  private final EventBus eventBus;
  private final SolrClusterStateUpdater solrClusterStateUpdater;


  @Autowired
  public SolrClusterStateMonitor(CuratorFramework curatorFramework,
                                 EventBus eventBus,
                                 SolrClusterStateUpdater solrClusterStateUpdater) {
    this.clusterStateNode = new NodeCache(curatorFramework, CLUSTER_STATE_ZNODE_PATH);
    this.eventBus = eventBus;
    this.solrClusterStateUpdater = solrClusterStateUpdater;
    eventBus.register(this);
  }

  @PostConstruct
  public void init() throws Exception {
    clusterStateNode.start(true);
    clusterStateNode.getListenable().addListener(this::onClusterStateUpdate);
  }

  @Subscribe
  public void onRefreshClusterState(RefreshClusterStatusEvent event) {
    onClusterStateUpdate();
  }

  private void onClusterStateUpdate() {
    ChildData currentData = clusterStateNode.getCurrentData();
    solrClusterStateUpdater.updateSolrClusterStateAsync(new String(currentData.getData()));
  }
}
