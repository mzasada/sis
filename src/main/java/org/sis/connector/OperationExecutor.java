package org.sis.connector;

import org.json.simple.JSONObject;
import org.sis.repl.bindings.operations.CreateCollection;
import org.sis.repl.bindings.operations.DropCollection;
import org.sis.repl.bindings.operations.FindOneDocument;
import org.sis.repl.bindings.operations.SaveDocument;

import java.util.concurrent.CompletableFuture;

public interface OperationExecutor {

  CompletableFuture<JSONObject> submit(CreateCollection operation);

  CompletableFuture<JSONObject> submit(DropCollection operation);

  CompletableFuture<JSONObject> submit(SaveDocument operation);

  CompletableFuture<JSONObject> submit(FindOneDocument operation);
}
