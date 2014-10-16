package org.sis.repl.bindings.operations;

import org.json.simple.JSONObject;
import org.sis.connector.OperationExecutor;

import java.util.concurrent.Future;

public interface Operation {

  Future<JSONObject> execute(OperationExecutor executor);
}
