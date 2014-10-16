package org.sis.connector.solr.api;

import org.json.simple.JSONObject;
import org.sis.connector.solr.cluster.ClusterState;
import org.sis.repl.bindings.operations.CreateCollectionOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class SolrCollectionServiceImpl implements SolrCollectionService {

  private static final String COLLECTIONS_API_ENDPOINT = "/admin/collections";

  private final AsyncRestOperations asyncRestOperations;
  private final ClusterState clusterState;

  @Autowired
  public SolrCollectionServiceImpl(AsyncRestOperations asyncRestOperations,
                                   ClusterState clusterState) {
    this.asyncRestOperations = asyncRestOperations;
    this.clusterState = clusterState;
  }

  @Override
  public CompletableFuture<JSONObject> create(CreateCollectionOperation operation) {
    CompletableFuture<JSONObject> response = new CompletableFuture<>();
    UriComponents uriComponents = UriComponentsBuilder
        .fromHttpUrl(getCollectionsApiEndpoint())
        .queryParam("action", "CREATE")
        .queryParam("name", operation.getCollectionName())
        .queryParam("numShards", operation.getShards())
        .build();

    asyncRestOperations.getForEntity(uriComponents.toUriString(), Map.class)
        .addCallback(new ListenableFutureCallback<ResponseEntity<Map>>() {
          @Override
          public void onSuccess(ResponseEntity<Map> result) {
            response.complete(new JSONObject(result.getBody()));
          }

          @Override
          public void onFailure(Throwable ex) {
            response.completeExceptionally(ex);
          }
        });
    return response;
  }

  private String getCollectionsApiEndpoint() {
    return clusterState.getAvailableNode().getHost() + COLLECTIONS_API_ENDPOINT;
  }
}
