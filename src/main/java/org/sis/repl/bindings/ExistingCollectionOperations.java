package org.sis.repl.bindings;

import org.json.simple.JSONObject;
import org.sis.repl.bindings.operations.DropCollection;
import org.sis.repl.bindings.operations.SaveDocument;

import java.util.Map;

public class ExistingCollectionOperations implements OperationsFacade {

  private final String collectionName;

  public ExistingCollectionOperations(String collectionName) {
    this.collectionName = collectionName;
  }

  public DropCollection delete() {
    return new DropCollection(collectionName);
  }

  public SaveDocument save(Map<String, Object> document) {
    return new SaveDocument(collectionName, document);
  }

  public JSONObject find(Map<String, Object> input) {
    JSONObject json = new JSONObject(input);
    System.out.println("input = [" + json + "]");
    return json;
  }
}
