package org.sis.repl.bindings;

import org.sis.repl.bindings.operations.CreateCollection;

/**
 * @since 1.0
 */
public class NewCollectionOperations implements OperationsFacade {

  private final String collectionName;

  public NewCollectionOperations(String collectionName) {
    this.collectionName = collectionName;
  }

  public CreateCollection create() {
    return new CreateCollection(collectionName);
  }
}
