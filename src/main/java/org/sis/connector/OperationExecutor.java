package org.sis.connector;

import org.json.simple.JSONObject;
import org.sis.repl.bindings.operations.CreateCollectionOperation;

import java.util.concurrent.Future;

public interface OperationExecutor {

  Future<JSONObject> submit(CreateCollectionOperation operation);
}
