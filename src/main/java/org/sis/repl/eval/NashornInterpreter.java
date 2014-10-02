package org.sis.repl.eval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

@Component
public class NashornInterpreter implements Interpreter {

  private static final Logger LOGGER = LoggerFactory.getLogger(NashornInterpreter.class);
  private final ScriptEngine scriptEngine;

  @Autowired
  public NashornInterpreter(ScriptEngine scriptEngine) {
    this.scriptEngine = scriptEngine;
  }

  @Override
  public Object eval(String input) {
    try {
      return scriptEngine.eval(input);
    } catch (ScriptException e) {
      LOGGER.info("Input couldn't be evaluated", e);
      return "Invalid input";
    }
  }
}
