package org.sis.connector.solr.api;

import org.json.simple.JSONObject;
import org.sis.repl.bindings.operations.FindOneDocument;
import org.sis.repl.bindings.operations.SaveDocument;

import java.util.concurrent.CompletableFuture;

public interface SolrDocumentService {

  CompletableFuture<JSONObject> save(SaveDocument operation);

  CompletableFuture<JSONObject> findOne(FindOneDocument operation);
}
