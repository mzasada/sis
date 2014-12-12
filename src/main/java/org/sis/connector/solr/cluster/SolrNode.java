package org.sis.connector.solr.cluster;

import static com.google.common.base.MoreObjects.toStringHelper;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

/**
 * Representation of a single Solr node in a cluster.
 *
 * @since 1.0
 */
public class SolrNode {

  private final String host;
  private final boolean leader;

  public SolrNode(String host, boolean leader) {
    this.host = host;
    this.leader = leader;
  }

  public String getHost() {
    return host;
  }

  public boolean isLeader() {
    return leader;
  }

  @Override
  public String toString() {
    return toStringHelper(this).add("host", host).add("leader", leader).toString();
  }

  @Override
  public boolean equals(Object obj) {
    return reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return reflectionHashCode(this);
  }
}
