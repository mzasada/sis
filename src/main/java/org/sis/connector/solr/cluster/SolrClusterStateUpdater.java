package org.sis.connector.solr.cluster;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import org.sis.connector.solr.api.SolrFileService;
import org.sis.connector.solr.cluster.config.SolrConfigXmlReader;
import org.sis.ipc.events.ClusterStatusUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.sis.connector.solr.cluster.util.ClusterTopologyUtils.findLeaderNode;

@Component
public class SolrClusterStateUpdater {

  private final EventBus eventBus;
  private final SolrFileService solrFileService;
  private final ExecutorService executorService;
  private final SolrClusterStateReader solrClusterStateReader;
  private final SolrConfigXmlReader solrConfigXmlReader;

  @Autowired
  public SolrClusterStateUpdater(EventBus eventBus, SolrFileService solrFileService) {
    this.eventBus = eventBus;
    this.solrFileService = solrFileService;
    this.executorService = Executors.newFixedThreadPool(5);
    this.solrClusterStateReader = new SolrClusterStateReader();
    this.solrConfigXmlReader = new SolrConfigXmlReader();
  }

  public void updateSolrClusterStateAsync(String clusterStateJson) {
    executorService.submit(() -> {
      Multimap<CollectionConfig, SolrNode> clusterTopology = HashMultimap.create();
      Multimap<String, SolrNode> clusterState = solrClusterStateReader.readClusterState(clusterStateJson);
      List<CompletableFuture<?>> pendingFutures = Lists.newArrayList();

      clusterState.asMap().forEach(
          (collection, nodes) ->
              pendingFutures.add(
                  loadCollectionConfig(collection, nodes)
                      .thenApply(solrConfigXmlReader::read)
                      .whenComplete((info, ex) ->
                          clusterTopology.putAll(new CollectionConfig(collection, info), nodes))));

      CompletableFuture.allOf(pendingFutures.toArray(new CompletableFuture[pendingFutures.size()])).join();

      eventBus.post(new ClusterStatusUpdateEvent(clusterTopology));
    });
  }

  @PreDestroy
  public void shutdown() throws InterruptedException {
    executorService.shutdown();
    executorService.awaitTermination(500, TimeUnit.MILLISECONDS);
  }

  private CompletableFuture<String> loadCollectionConfig(String collectionName, Collection<SolrNode> nodes) {
    return findLeaderNode(nodes)
        .map(node -> solrFileService.fetchSolrConfigXml(node, collectionName))
        .orElseThrow(() -> new IllegalStateException(""));
  }
}
