package org.sis.repl.bindings;

import org.json.simple.JSONObject;

import java.util.Map;

public class CollectionOperations {

  public JSONObject find(Map<String, Object> input) {
    JSONObject json = new JSONObject(input);
    System.out.println("input = [" + json + "]");
    return json;
  }
}
