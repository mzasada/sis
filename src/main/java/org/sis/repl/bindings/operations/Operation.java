package org.sis.repl.bindings.operations;

import org.sis.connector.OperationExecutor;

import java.util.concurrent.Future;

public interface Operation {

  Future<String> execute(OperationExecutor executor);
}
