package org.sis.connector.solr.cluster.config

import org.sis.connector.solr.cluster.config.SolrClusterStateReader
import spock.lang.Specification

class SolrClusterStateReaderTest extends Specification {

  SolrClusterStateReader clusterStateReader = new SolrClusterStateReader()

  def 'should read all collections from cluster state descriptor'() {
    given:
    def json = getClass().getResource("/clusterstate.json").text

    when:
    def state = clusterStateReader.readClusterState(json)

    then:
    state.keySet() == ['collection1'] as Set
  }
}
