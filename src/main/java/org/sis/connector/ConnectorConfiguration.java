package org.sis.connector;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.AsyncRestTemplate;

@Configuration
public class ConnectorConfiguration {

  @Bean
  public AsyncRestTemplate asyncRestTemplate() {
    return new AsyncRestTemplate();
  }
}
