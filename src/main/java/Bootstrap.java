import org.sis.repl.ReplRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.sis")
@EnableAutoConfiguration
public class Bootstrap {

  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(Bootstrap.class, args);
    ctx.getBean(ReplRunner.class).startLoop();
  }
}
