package org.sis.repl;

import jline.TerminalFactory;
import jline.console.ConsoleReader;
import org.sis.repl.bindings.CollectionOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.io.IOException;

@Configuration
public class ReplConfiguration {

  private static final String SHELL_PROMPT = "> ";
  private static final String COLLECTION_LOOKUP_BINDING = "sc.books";

  @Bean
  public ConsoleReader consoleReader() throws IOException {
    TerminalFactory.configure(TerminalFactory.AUTO);
    TerminalFactory.reset();
    jline.internal.Configuration.reset();
    ConsoleReader reader = new ConsoleReader();
    reader.setPrompt(SHELL_PROMPT);
    return reader;
  }


  @Bean
  public ScriptEngine scriptEngine() {
    ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
    scriptEngine.setBindings(bindings(), ScriptContext.ENGINE_SCOPE);
    return scriptEngine;
  }

  private Bindings bindings() {
    Bindings bindings = new SimpleBindings();
    bindings.put(COLLECTION_LOOKUP_BINDING, new CollectionOperations());
    return bindings;
  }
}
