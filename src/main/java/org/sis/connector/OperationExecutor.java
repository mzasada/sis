package org.sis.connector;

import org.json.simple.JSONObject;
import org.sis.repl.bindings.operations.CreateCollectionOperation;

import java.util.concurrent.CompletableFuture;

public interface OperationExecutor {

  CompletableFuture<JSONObject> submit(CreateCollectionOperation operation);
}
