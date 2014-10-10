package org.sis.repl.bindings.operations;

import org.sis.connector.OperationExecutor;

import java.util.concurrent.Future;

public class CreateCollectionOperation implements Operation {

  private final String collectionName;

  public CreateCollectionOperation(String collectionName) {
    this.collectionName = collectionName;
  }

  public CreateCollectionOperation replicas(long count) {
    System.out.println("replicas count = " + count);
    return this;
  }

  public CreateCollectionOperation shards(long count) {
    System.out.println("shards count = " + count);
    return this;
  }

  @Override
  public Future<String> execute(OperationExecutor executor) {
    return executor.submit(this);
  }
}
