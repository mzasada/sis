package org.sis.repl;

import jline.TerminalFactory;
import jline.console.ConsoleReader;
import org.sis.repl.bindings.CollectionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.script.*;
import java.io.IOException;

@Configuration
public class ReplConfiguration {

  private static final String SHELL_PROMPT = "> ";
  private static final String COLLECTION_LOOKUP_BINDING = "sc";

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
  public ScriptEngine scriptEngine(CollectionRegistry collectionRegistry) {
    ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
    scriptEngine.setBindings(bindings(collectionRegistry), ScriptContext.ENGINE_SCOPE);
    return scriptEngine;
  }

  private Bindings bindings(CollectionRegistry collectionRegistry) {
    Bindings bindings = new SimpleBindings();
    bindings.put(COLLECTION_LOOKUP_BINDING, collectionRegistry.getCurrentCollectionsView());
    return bindings;
  }
}
