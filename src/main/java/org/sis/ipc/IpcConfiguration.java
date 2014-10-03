package org.sis.ipc;

import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IpcConfiguration {

  @Bean
  public EventBus eventBus() {
    return new EventBus();
  }
}
