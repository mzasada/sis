import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import org.sis.ipc.IpcConfiguration;
import org.sis.ipc.events.ClusterStatusUpdateEvent;
import org.sis.repl.ReplConfiguration;
import org.sis.repl.ReplRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    ReplConfiguration.class,
    IpcConfiguration.class})
public class Bootstrap {

  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(Bootstrap.class, args);
    ReplRunner replRunner = ctx.getBean(ReplRunner.class);
    EventBus eventBus = ctx.getBean(EventBus.class);
    eventBus.post(new ClusterStatusUpdateEvent(ImmutableSet.of("books", "bikes", "shoes")));
    replRunner.startLoop();
  }
}
