package org.sis.repl.bindings.operations;

import org.json.simple.JSONObject;
import org.sis.connector.OperationExecutor;

import java.util.concurrent.CompletableFuture;

/**
 * Command for creating a single collection.
 *
 * @since 1.0
 */
public class CreateCollection extends NamedCollectionOperation {

  private static final long MIN_SHARD_COUNT = 1L;

  private long replicas;
  private long shards = MIN_SHARD_COUNT;

  public CreateCollection(String collectionName) {
    super(collectionName);
  }

  public CreateCollection replicas(long count) {
    replicas = count;
    return this;
  }

  public CreateCollection shards(long count) {
    shards = count;
    return this;
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
