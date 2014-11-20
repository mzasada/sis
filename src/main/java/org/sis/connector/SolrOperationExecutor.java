package org.sis.connector;

import org.json.simple.JSONObject;
import org.sis.connector.solr.api.SolrCollectionService;
import org.sis.repl.bindings.operations.CreateCollectionOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class SolrOperationExecutor implements OperationExecutor {

  private final SolrCollectionService solrCollectionService;

  @Autowired
  public SolrOperationExecutor(SolrCollectionService solrCollectionService) {
    this.solrCollectionService = solrCollectionService;
  }

  @Override
  public CompletableFuture<JSONObject> submit(CreateCollectionOperation operation) {
    return solrCollectionService.create(operation);
  }
}
