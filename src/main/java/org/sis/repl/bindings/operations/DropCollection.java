package org.sis.repl.bindings.operations;

import org.json.simple.JSONObject;
import org.sis.connector.OperationExecutor;

import java.util.concurrent.CompletableFuture;

public class DropCollection implements Operation {

  private final String collectionName;

  public DropCollection(String collectionName) {
    this.collectionName = collectionName;
  }

  @Override
  public CompletableFuture<JSONObject> execute(OperationExecutor executor) {
    return executor.submit(this);
  }

  public String getCollectionName() {
    return collectionName;
  }
}
