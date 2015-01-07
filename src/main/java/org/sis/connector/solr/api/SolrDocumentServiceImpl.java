package org.sis.connector.solr.api;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.sis.connector.solr.api.messages.SearchHandlerResponseConverter;
import org.sis.connector.solr.cluster.ClusterState;
import org.sis.connector.solr.cluster.CollectionConfig;
import org.sis.connector.solr.cluster.SolrNode;
import org.sis.connector.solr.cluster.config.HandlersInfo;
import org.sis.repl.bindings.operations.FindOneDocument;
import org.sis.repl.bindings.operations.SaveDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.google.common.collect.ImmutableList.of;
import static org.sis.util.concurrent.Futures.completableFuture;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class SolrDocumentServiceImpl implements SolrDocumentService {
  private static final Logger LOGGER = LoggerFactory.getLogger(SolrDocumentServiceImpl.class);
  private static final Gson GSON = new Gson();

  private final SearchHandlerResponseConverter searchHandlerResponseConverter = new SearchHandlerResponseConverter();
  private final AsyncRestOperations asyncRestOperations;
  private final ClusterState clusterState;

  @Autowired
  public SolrDocumentServiceImpl(AsyncRestOperations asyncRestOperations, ClusterState clusterState) {
    this.asyncRestOperations = asyncRestOperations;
    this.clusterState = clusterState;
  }

  @Override
  public CompletableFuture<JSONObject> save(SaveDocument operation) {
    UriComponents uriComponents = UriComponentsBuilder
        .fromHttpUrl(getHandlerEndpoint(operation.getCollectionName(), HandlersInfo::getUpdateHandler))
        .queryParam("commit", true) //TODO: make it explicit options for user -> commit + openSearcher
        .queryParam("json.command", false)
        .queryParam("wt", "json")
        .build();
    String payload = GSON.toJson(operation.getDocument());

    return sendAsyncRequest(uriComponents.toUriString(), payload);
  }

  @Override
  public CompletableFuture<JSONObject> findOne(FindOneDocument operation) {
    UriComponents uriComponents = UriComponentsBuilder
        .fromHttpUrl(getHandlerEndpoint(operation.getCollectionName(), HandlersInfo::getSearchHandler))
        .queryParam("q", "*:*")
        .queryParam("fq", buildFilterQuery(operation.getCriteria()))
        .queryParam("start", "0")
        .queryParam("rows", "1")
        .queryParam("wt", "json")
        .build();

    return completableFuture(asyncRestOperations.getForEntity(uriComponents.toUriString(), Map.class))
        .thenApply(r -> new JSONObject(r.getBody()));
  }

  private CompletableFuture<JSONObject> sendAsyncRequest(String uri, String payload) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(of(APPLICATION_JSON));
    httpHeaders.setContentType(APPLICATION_JSON);

    return completableFuture(asyncRestOperations.postForEntity(uri, new HttpEntity<>(payload, httpHeaders), String.class))
        .thenApply(this::getUpdateResponseAsJSON);
  }

  private String[] buildFilterQuery(Map<String, Object> criteria) {
    List<String> queryParams = Lists.newArrayList();
    criteria.forEach((k, v) -> queryParams.add(String.format("%s:%s", k, v)));
    return queryParams.toArray(new String[queryParams.size()]);
  }

  private JSONObject getUpdateResponseAsJSON(ResponseEntity<String> response) {
    return searchHandlerResponseConverter.convert(response.getBody());
  }

  private String getHandlerEndpoint(String collectionName, Function<HandlersInfo, String> handlerExtraction) {
    String searchHandler = clusterState.findCollectionConfigByName(collectionName)
        .map(CollectionConfig::getHandlersInfo)
        .map(handlerExtraction)
        .orElseThrow(() -> {
          LOGGER.warn("Inconsistent collection view - could not find any collection with name: '{}'", collectionName);
          return new IllegalStateException();
        });

    return clusterState.findLeaderNodeForCollection(collectionName)
        .map(SolrNode::getHost)
        .map(host -> String.format("%s/%s/%s", host, collectionName, searchHandler))
        .orElseThrow(() -> {
          LOGGER.warn("Could not find leader node for collection '{}'", collectionName);
          return new IllegalStateException();
        });
  }
}
