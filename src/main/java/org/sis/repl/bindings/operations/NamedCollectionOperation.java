package org.sis.repl.bindings.operations;

/**
 * Base command to be executed against a given collection.
 *
 * @since 1.0
 */
public abstract class NamedCollectionOperation implements Operation {

  private final String collectionName;

  protected NamedCollectionOperation(String collectionName) {
    this.collectionName = collectionName;
  }

  public String getCollectionName() {
    return collectionName;
  }
}
