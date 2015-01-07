package org.sis.repl.bindings.operations;

import org.json.simple.JSONObject;
import org.sis.connector.OperationExecutor;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Command for finding a single document.
 *
 * @since 1.0
 */
public class FindOneDocument extends NamedCollectionOperation {

  private final Map<String, Object> criteria;

  public FindOneDocument(String collectionName, Map<String, Object> criteria) {
    super(collectionName);
    this.criteria = criteria;
  }

  @Override
  public CompletableFuture<JSONObject> execute(OperationExecutor executor) {
    return executor.submit(this);
  }

  public Map<String, Object> getCriteria() {
    return criteria;
  }
}
