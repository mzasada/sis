package org.sis.repl.bindings;

import org.json.simple.JSONObject;
import org.sis.repl.bindings.operations.CreateCollectionOperation;

import java.util.Map;

public class CollectionOperations {

  private final String collectionName;

  public CollectionOperations(String collectionName) {
    this.collectionName = collectionName;
  }

  public CreateCollectionOperation create() {
    return new CreateCollectionOperation(collectionName);
  }

  public JSONObject find(Map<String, Object> input) {
    JSONObject json = new JSONObject(input);
    System.out.println("input = [" + json + "]");
    return json;
  }
}
