package org.sis.repl.bindings.operations;

public class CreateCollectionOperation {

  private final String collectionName;

  public CreateCollectionOperation(String collectionName) {
    this.collectionName = collectionName;
  }

  public CreateCollectionOperation replicas(long count) {
    System.out.println("count = " + count);
    return this;
  }

  public CreateCollectionOperation shards(long count) {
    System.out.println("count = " + count);
    return this;
  }
}
