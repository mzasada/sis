package org.sis.connector.solr.api;

import org.json.simple.JSONObject;
import org.sis.repl.bindings.operations.CreateCollectionOperation;

import java.util.concurrent.CompletableFuture;

public interface SolrCollectionService {
  CompletableFuture<JSONObject> create(CreateCollectionOperation createCollectionOperation);
}
