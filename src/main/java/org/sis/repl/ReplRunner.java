package org.sis.repl;

import com.google.common.eventbus.EventBus;
import jline.console.ConsoleReader;
import org.sis.ipc.events.RefreshClusterStatusEvent;
import org.sis.repl.eval.Interpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class ReplRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReplRunner.class);

  private final ConsoleReader in;
  private final PrintWriter out;
  private final Interpreter interpreter;
  private final EventBus eventBus;

  @Autowired
  public ReplRunner(ConsoleReader in, Interpreter interpreter, EventBus eventBus) {
    this.in = in;
    this.eventBus = eventBus;
    this.out = new PrintWriter(in.getOutput());
    this.interpreter = interpreter;
  }

  public void startLoop() {
    eventBus.post(new RefreshClusterStatusEvent());
    try {
      String input;
      while ((input = in.readLine()) != null) {
        out.println(interpreter.eval(input));
        out.flush();
      }
    } catch (IOException e) {
      LOGGER.error("Could not read value from standard input", e);
    } finally {
      out.close();
    }
  }
}
