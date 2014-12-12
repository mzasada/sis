package org.sis.repl.bindings.operations;

import org.json.simple.JSONObject;
import org.sis.connector.OperationExecutor;

import java.util.concurrent.CompletableFuture;

public class SaveDocument implements Operation {

  private final String collectionName;
  private final JSONObject document;

  public SaveDocument(String collectionName, JSONObject document) {
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
}
