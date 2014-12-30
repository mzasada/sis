package org.sis.connector;

import org.json.simple.JSONObject;
import org.sis.connector.solr.api.SolrCollectionService;
import org.sis.connector.solr.api.SolrDocumentService;
import org.sis.repl.bindings.operations.CreateCollection;
import org.sis.repl.bindings.operations.DropCollection;
import org.sis.repl.bindings.operations.SaveDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static org.sis.util.concurrent.Futures.within;

@Component
public class SolrOperationExecutor implements OperationExecutor {

  private static final Duration DEFAULT_OPERATION_TIMEOUT = Duration.ofSeconds(1);

  private final SolrCollectionService solrCollectionService;
  private final SolrDocumentService solrDocumentService;

  @Autowired
  public SolrOperationExecutor(SolrCollectionService solrCollectionService, SolrDocumentService solrDocumentService) {
    this.solrCollectionService = solrCollectionService;
    this.solrDocumentService = solrDocumentService;
  }

  @Override
  public CompletableFuture<JSONObject> submit(CreateCollection operation) {
    return within(solrCollectionService.create(operation), DEFAULT_OPERATION_TIMEOUT);
  }

  @Override
  public CompletableFuture<JSONObject> submit(DropCollection operation) {
    return within(solrCollectionService.drop(operation), DEFAULT_OPERATION_TIMEOUT);
  }

  @Override
  public CompletableFuture<JSONObject> submit(SaveDocument operation) {
    return within(solrDocumentService.save(operation), DEFAULT_OPERATION_TIMEOUT);
  }
}
