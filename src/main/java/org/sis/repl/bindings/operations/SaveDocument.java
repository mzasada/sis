package org.sis.repl.bindings.operations;

import org.json.simple.JSONObject;
import org.sis.connector.OperationExecutor;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SaveDocument implements Operation {

  private final String collectionName;
  private final Map<String, Object> document;

  public SaveDocument(String collectionName, Map<String, Object> document) {
    this.collectionName = collectionName;
    this.document = document;
  }

  @Override
  public CompletableFuture<JSONObject> execute(OperationExecutor executor) {
    return executor.submit(this);
  }

  public String getCollectionName() {
    return collectionName;
  }

  public Map<String, Object> getDocument() {
    return document;
  }
}
