package org.sis.repl.bindings.operations;

import org.json.simple.JSONObject;
import org.sis.connector.OperationExecutor;

import java.util.concurrent.CompletableFuture;

/**
 * Command for deleting entire collection by name.
 *
 * @since 1.0
 */
public class DropCollection extends NamedCollectionOperation {

  public DropCollection(String collectionName) {
    super(collectionName);
  }

  @Override
  public CompletableFuture<JSONObject> execute(OperationExecutor executor) {
    return executor.submit(this);
  }
}
