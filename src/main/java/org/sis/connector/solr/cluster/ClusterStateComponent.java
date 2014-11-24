package org.sis.connector.solr.cluster;

import org.springframework.stereotype.Component;

@Component
public class ClusterStateComponent implements ClusterState {

  @Override
  public SolrNode getAvailableNode() {
    // TODO: fixme
    return new SolrNode("http://localhost:9001/solr");
  }
}
