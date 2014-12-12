package org.sis.repl.bindings;

import org.json.simple.JSONObject;
import org.sis.repl.bindings.operations.CreateCollection;
import org.sis.repl.bindings.operations.DropCollection;
import org.sis.repl.bindings.operations.SaveDocument;

import java.util.Map;

public class OperationsFacade {

  private final String collectionName;

  public OperationsFacade(String collectionName) {
    this.collectionName = collectionName;
  }

  public CreateCollection create() {
    return new CreateCollection(collectionName);
  }

  public DropCollection delete() {
    return new DropCollection(collectionName);
  }

  public SaveDocument save(Map<String, Object> document) {
    return new SaveDocument(collectionName, new JSONObject(document));
  }

  public JSONObject find(Map<String, Object> input) {
    JSONObject json = new JSONObject(input);
    System.out.println("input = [" + json + "]");
    return json;
  }
}
