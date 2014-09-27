import jline.TerminalFactory;
import jline.console.ConsoleReader;
import jline.console.history.MemoryHistory;
import jline.internal.Configuration;

import java.io.IOException;
import java.io.PrintWriter;

public class Bootstrap {

  public static void main(String[] args) {
    try {
      TerminalFactory.configure(TerminalFactory.AUTO);
      TerminalFactory.reset();
      Configuration.reset();

      ConsoleReader reader = new ConsoleReader();
      reader.setPrompt("> ");
      reader.setHistory(new MemoryHistory());

      PrintWriter out = new PrintWriter(reader.getOutput());

      String input;
      while ((input = reader.readLine()) != null) {
        out.println("input = " + input);
        out.flush();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
