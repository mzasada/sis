package org.sis.repl.bindings.operations;

import org.json.simple.JSONObject;
import org.sis.connector.OperationExecutor;

import java.util.concurrent.CompletableFuture;

public interface Operation {

  CompletableFuture<JSONObject> execute(OperationExecutor executor);
}
