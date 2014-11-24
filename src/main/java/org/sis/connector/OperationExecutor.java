package org.sis.connector;

import org.json.simple.JSONObject;
import org.sis.repl.bindings.operations.CreateCollection;
import org.sis.repl.bindings.operations.DeleteCollection;

import java.util.concurrent.CompletableFuture;

public interface OperationExecutor {

  CompletableFuture<JSONObject> submit(CreateCollection operation);

  CompletableFuture<JSONObject> submit(DeleteCollection operation);
}
