package org.sis.connector;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;

@Configuration
public class ConnectorConfiguration {

  private static final int HTTP_READ_TIMEOUT_IN_MILLIS = 2_000;
  private static final int HTTP_CONNECT_TIMEOUT_IN_MILLIS = 2_000;

  @Bean
  @Autowired
  public AsyncRestTemplate asyncRestTemplate(AsyncClientHttpRequestFactory asyncClientHttpRequestFactory) {
    return new AsyncRestTemplate(asyncClientHttpRequestFactory);
  }

  @Bean
  public AsyncClientHttpRequestFactory asyncClientHttpRequestFactory() {
    HttpComponentsAsyncClientHttpRequestFactory factory = new HttpComponentsAsyncClientHttpRequestFactory();
    factory.setReadTimeout(HTTP_READ_TIMEOUT_IN_MILLIS);
    factory.setConnectTimeout(HTTP_CONNECT_TIMEOUT_IN_MILLIS);
    return factory;
  }

  @Bean(initMethod = "start", destroyMethod = "close")
  public CuratorFramework curatorFramework() {
    return CuratorFrameworkFactory.newClient("localhost:9001", new ExponentialBackoffRetry(1000, 3));
  }
}
