package org.sis.connector.solr.cluster.config

import spock.lang.Specification

class SolrConfigXmlReaderTest extends Specification {

  SolrConfigXmlReader solrConfigXmlReader = new SolrConfigXmlReader()

  def 'should read search handler with the simpliest configuration as a default collection handler'() {
    given:
    def solrConfig = getClass().getResource("/solrconfig.xml").text

    when:
    def handlers = solrConfigXmlReader.read(solrConfig)

    then:
    handlers.searchHandler == "/select"
  }
}
