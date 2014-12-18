package org.sis.connector.solr.api;

import org.json.simple.JSONObject;
import org.sis.connector.solr.cluster.ClusterState;
import org.sis.connector.solr.cluster.CollectionConfig;
import org.sis.connector.solr.cluster.SolrNode;
import org.sis.repl.bindings.operations.SaveDocument;
import org.springframework.web.client.AsyncRestOperations;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SolrDocumentServiceImpl implements SolrDocumentService {

  private final AsyncRestOperations asyncRestOperations;
  private final ClusterState clusterState;

  public SolrDocumentServiceImpl(AsyncRestOperations asyncRestOperations, ClusterState clusterState) {
    this.asyncRestOperations = asyncRestOperations;
    this.clusterState = clusterState;
  }

  @Override
  public CompletableFuture<JSONObject> save(SaveDocument operation) {
    Optional<CollectionConfig> collectionConfig = clusterState.findCollectionConfigByName(operation.getCollectionName());

    Optional<SolrNode> leaderNode = clusterState.findLeaderNodeForCollection(operation.getCollectionName());
    operation.getCollectionName();

    asyncRestOperations.getForEntity();

    return null;
  }
}
