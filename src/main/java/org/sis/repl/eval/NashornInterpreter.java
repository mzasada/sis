package org.sis.repl.eval;

import org.sis.connector.OperationExecutor;
import org.sis.repl.bindings.operations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.concurrent.ExecutionException;

@Component
public class NashornInterpreter implements Interpreter {

  private static final Logger LOGGER = LoggerFactory.getLogger(NashornInterpreter.class);
  private final ScriptEngine scriptEngine;
  private final OperationExecutor operationExecutor;

  @Autowired
  public NashornInterpreter(ScriptEngine scriptEngine, OperationExecutor operationExecutor) {
    this.scriptEngine = scriptEngine;
    this.operationExecutor = operationExecutor;
  }

  @Override
  public Object eval(String input) {
    try {
      Object result = scriptEngine.eval(input);
      if (result instanceof Operation) {
        // TODO: pretty JSON?
        return ((Operation) result).execute(operationExecutor).get();
      } else {
        //TODO: pretty JSON?
        return result;
      }
    } catch (ScriptException e) {
      LOGGER.info("Input couldn't be evaluated", e);
      return "Invalid input";
    } catch (InterruptedException | ExecutionException e) {
      LOGGER.info("Execution failed", e);
      return "Execution failed";
    }
  }
}
