package org.sis.connector;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.AsyncRestTemplate;

@Configuration
public class ConnectorConfiguration {

  @Bean
  public AsyncRestTemplate asyncRestTemplate() {
    return new AsyncRestTemplate();
  }

  @Bean(initMethod = "start", destroyMethod = "close")
  public CuratorFramework curatorFramework() {
    return CuratorFrameworkFactory.newClient("localhost:9001", new ExponentialBackoffRetry(1000, 3));
  }
}
