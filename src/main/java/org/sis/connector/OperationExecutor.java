package org.sis.connector;

import org.sis.repl.bindings.operations.CreateCollectionOperation;

import java.util.concurrent.Future;

public interface OperationExecutor {

  Future<String> submit(CreateCollectionOperation operation);
}
