package org.sis.repl.bindings.operations;

import org.json.simple.JSONObject;
import org.sis.connector.OperationExecutor;

import java.util.concurrent.CompletableFuture;

public class CreateCollection implements Operation {

  private static final long MIN_SHARD_COUNT = 1L;

  private final String collectionName;
  private long replicas;
  private long shards = MIN_SHARD_COUNT;

  public CreateCollection(String collectionName) {
    this.collectionName = collectionName;
  }

  public CreateCollection replicas(long count) {
    replicas = count;
    return this;
  }

  public CreateCollection shards(long count) {
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
  public CompletableFuture<JSONObject> execute(OperationExecutor executor) {
    return executor.submit(this);
  }
}
