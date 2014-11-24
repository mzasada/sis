package org.sis.connector.solr.api;

import org.json.simple.JSONObject;
import org.sis.repl.bindings.operations.CreateCollection;
import org.sis.repl.bindings.operations.DeleteCollection;

import java.util.concurrent.CompletableFuture;

public interface SolrCollectionService {
  CompletableFuture<JSONObject> create(CreateCollection operation);

  CompletableFuture<JSONObject> delete(DeleteCollection operation);
}
