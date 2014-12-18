package org.sis.connector.solr.api;

import org.json.simple.JSONObject;
import org.sis.connector.solr.cluster.ClusterState;
import org.sis.connector.solr.cluster.CollectionConfig;
import org.sis.connector.solr.cluster.SolrNode;
import org.sis.connector.solr.cluster.config.HandlersInfo;
import org.sis.repl.bindings.operations.SaveDocument;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.google.common.collect.ImmutableList.of;
import static org.sis.util.concurrent.Futures.completableFuture;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class SolrDocumentServiceImpl implements SolrDocumentService {

  private final AsyncRestOperations asyncRestOperations;
  private final ClusterState clusterState;

  public SolrDocumentServiceImpl(AsyncRestOperations asyncRestOperations, ClusterState clusterState) {
    this.asyncRestOperations = asyncRestOperations;
    this.clusterState = clusterState;
  }

  @Override
  public CompletableFuture<JSONObject> save(SaveDocument operation) {
    UriComponents uriComponents = UriComponentsBuilder
        .fromHttpUrl(getSearchHandlerEndpoint(operation.getCollectionName()))
        .queryParam("q", "????")
        .queryParam("wt", "json")
        .build();

    return sendAsyncRequest(uriComponents.toUriString());
  }

  private CompletableFuture<JSONObject> sendAsyncRequest(String uri) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(of(APPLICATION_JSON));

    return completableFuture(asyncRestOperations.exchange(uri, HttpMethod.GET, new HttpEntity(httpHeaders), Map.class))
        .thenApply(r -> new JSONObject(r.getBody()));
  }

  private String getSearchHandlerEndpoint(String collectionName) {
    String searchHandler = clusterState.findCollectionConfigByName(collectionName)
        .map(CollectionConfig::getHandlersInfo)
        .map(HandlersInfo::getSearchHandler)
        .orElseThrow(() -> new IllegalStateException());

    return clusterState.findLeaderNodeForCollection(collectionName)
        .map(SolrNode::getHost)
        .map(host -> String.format("%s/%s", host, searchHandler))
        .orElseThrow(() -> new IllegalStateException());
  }
}
