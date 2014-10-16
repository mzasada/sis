package org.sis.repl.bindings.operations;

import org.json.simple.JSONObject;
import org.sis.connector.OperationExecutor;

import java.util.concurrent.Future;

public class CreateCollectionOperation implements Operation {

  private final String collectionName;
  private long replicas;
  private long shards;

  public CreateCollectionOperation(String collectionName) {
    this.collectionName = collectionName;
  }

  public CreateCollectionOperation replicas(long count) {
    replicas = count;
    return this;
  }

  public CreateCollectionOperation shards(long count) {
    shards = count;
    return this;
  }

  public String getCollectionName() {
    return collectionName;
  }

  public long getReplicas() {
    return replicas;
  }

  public long getShards() {
    return shards;
  }

  @Override
  public Future<JSONObject> execute(OperationExecutor executor) {
    return executor.submit(this);
  }
}
