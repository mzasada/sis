package org.sis.connector.solr.api;

import org.json.simple.JSONObject;
import org.sis.connector.solr.cluster.ClusterState;
import org.sis.connector.solr.cluster.SolrNode;
import org.sis.repl.bindings.operations.CreateCollection;
import org.sis.repl.bindings.operations.DropCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.google.common.collect.ImmutableList.of;
import static org.sis.util.concurrent.Futures.completableFuture;
import static org.springframework.http.MediaType.APPLICATION_JSON;

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
  public CompletableFuture<JSONObject> create(CreateCollection operation) {
    UriComponents uriComponents = UriComponentsBuilder
        .fromHttpUrl(getCollectionsApiEndpoint())
        .queryParam("action", "CREATE")
        .queryParam("wt", "json")
        .queryParam("name", operation.getCollectionName())
        .queryParam("numShards", operation.getShards())
        .build();

    return sendAsyncRequest(uriComponents.toUriString());
  }

  @Override
  public CompletableFuture<JSONObject> drop(DropCollection operation) {
    UriComponents uriComponents = UriComponentsBuilder
        .fromHttpUrl(getCollectionsApiEndpoint())
        .queryParam("action", "DELETE")
        .queryParam("wt", "json")
        .queryParam("name", operation.getCollectionName())
        .build();

    return sendAsyncRequest(uriComponents.toUriString());
  }

  private CompletableFuture<JSONObject> sendAsyncRequest(String uri) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(of(APPLICATION_JSON));

    return completableFuture(asyncRestOperations.exchange(uri, HttpMethod.GET, new HttpEntity(httpHeaders), Map.class))
        .thenApply(r -> new JSONObject(r.getBody()));
  }

  private String getCollectionsApiEndpoint() {
    return clusterState.findAnyAvailableNode()
        .map(SolrNode::getHost)
        .orElseThrow(() -> new IllegalStateException("")) + COLLECTIONS_API_ENDPOINT;
  }
}
