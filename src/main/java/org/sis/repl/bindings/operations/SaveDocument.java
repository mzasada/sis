package org.sis.repl.bindings.operations;

import org.json.simple.JSONObject;
import org.sis.connector.OperationExecutor;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Command for saving single document.
 *
 * @since 1.0
 */
public class SaveDocument extends NamedCollectionOperation {

  private final Map<String, Object> document;

  public SaveDocument(String collectionName, Map<String, Object> document) {
    super(collectionName);
    this.document = document;
  }

  @Override
  public CompletableFuture<JSONObject> execute(OperationExecutor executor) {
    return executor.submit(this);
  }

  public Map<String, Object> getDocument() {
    return document;
  }
}
