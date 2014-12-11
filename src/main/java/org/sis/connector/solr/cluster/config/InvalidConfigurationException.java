package org.sis.connector.solr.cluster.config;

/**
 * Thrown when configuration read from ZooKeeper or Solr is insufficient to perform full set of operations.
 *
 * @since 1.0
 */
public class InvalidConfigurationException extends RuntimeException {

  public InvalidConfigurationException(String message) {
    super(message);
  }
}
