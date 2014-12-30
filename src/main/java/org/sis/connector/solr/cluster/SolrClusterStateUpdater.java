package org.sis.connector.solr.cluster;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import org.sis.connector.solr.api.SolrFileService;
import org.sis.connector.solr.cluster.config.SolrClusterStateReader;
import org.sis.connector.solr.cluster.config.SolrConfigXmlReader;
import org.sis.ipc.events.ClusterStatusUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.sis.connector.solr.cluster.util.ClusterTopologyUtils.findLeaderNode;

@Component
public class SolrClusterStateUpdater {

  private static final Logger LOGGER = LoggerFactory.getLogger(SolrClusterStateUpdater.class);

  private final EventBus eventBus;
  private final SolrFileService solrFileService;
  private final SolrClusterStateReader solrClusterStateReader;
  private final SolrConfigXmlReader solrConfigXmlReader;

  @Autowired
  public SolrClusterStateUpdater(EventBus eventBus, SolrFileService solrFileService) {
    this.eventBus = eventBus;
    this.solrFileService = solrFileService;
    this.solrClusterStateReader = new SolrClusterStateReader();
    this.solrConfigXmlReader = new SolrConfigXmlReader();
  }

  public void updateSolrClusterStateAsync(String clusterStateJson) {
    Multimap<CollectionConfig, SolrNode> clusterTopology = HashMultimap.create();
    Multimap<String, SolrNode> clusterState = solrClusterStateReader.readClusterState(clusterStateJson);
    LOGGER.debug("Cluster state changed. Current view - '{}'", clusterState);

    List<CompletableFuture<?>> pendingFutures = Lists.newArrayList();
    findOnlyCollectionLeaders(clusterState).forEach(
        (collection, node) -> pendingFutures.add(
            solrFileService.fetchSolrConfigXml(node, collection)
                .thenApply(solrConfigXmlReader::read)
                .thenAccept(info ->
                    clusterTopology.putAll(
                        new CollectionConfig(collection, info), clusterState.get(collection)))));

    CompletableFuture.allOf(pendingFutures.toArray(new CompletableFuture[pendingFutures.size()])).join();

    eventBus.post(new ClusterStatusUpdateEvent(clusterTopology));
  }

  private Map<String, SolrNode> findOnlyCollectionLeaders(Multimap<String, SolrNode> clusterState) {
    Map<String, SolrNode> collectionLeaders = new HashMap<>();
    clusterState.asMap().forEach(
        (collection, nodes) -> findLeaderNode(nodes)
            // Skip leaderless collections, since they might be unavailable. Cluster will eventually converge
            // and when it happens, curator will rerun the whole sync process all over.
            .ifPresent(
                node -> collectionLeaders.put(collection, node))
    );
    return collectionLeaders;
  }
}
