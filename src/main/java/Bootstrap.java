import org.sis.repl.ReplConfiguration;
import org.sis.repl.ReplRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = ReplConfiguration.class)
public class Bootstrap {

  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(Bootstrap.class, args);
    ReplRunner replRunner = ctx.getBean(ReplRunner.class);
    replRunner.startLoop();
  }
}
