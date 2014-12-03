package org.sis.connector.solr.cluster;

import static com.google.common.base.MoreObjects.toStringHelper;

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
}
