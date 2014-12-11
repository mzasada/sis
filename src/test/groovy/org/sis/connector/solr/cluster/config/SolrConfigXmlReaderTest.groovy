package org.sis.connector.solr.cluster.config

import spock.lang.Specification

class SolrConfigXmlReaderTest extends Specification {

  SolrConfigXmlReader solrConfigXmlReader = new SolrConfigXmlReader()

  def 'should read search handler with the simplest configuration as a default collection handler'() {
    given:
    def config =
        """
      <?xml version="1.0" encoding="UTF-8" ?>
      <config>
        <requestHandler name="/get" class="solr.RealTimeGetHandler" />
        <requestHandler name="/query" class="solr.SearchHandler">
          <lst name="defaults">
              <str name="echoParams">explicit</str>
              <str name="wt">json</str>
              <str name="indent">true</str>
              <str name="df">text</str>
          </lst>
        </requestHandler>
        <requestHandler name="/simple" class="solr.SearchHandler">
          <lst name="defaults">
              <str name="wt">json</str>
          </lst>
        </requestHandler>
      </config>
        """

    when:
    def handlers = solrConfigXmlReader.read(config)

    then:
    handlers.searchHandler == "/simple"

  }

  def 'should pick only search handlers when looking for a search handler'() {
    given:
    def config =
        """
      <?xml version="1.0" encoding="UTF-8" ?>
      <config>
        <requestHandler name="/get" class="solr.RealTimeGetHandler" />
      </config>
        """

    when:
    solrConfigXmlReader.read(config)

    then:
    thrown(InvalidConfigurationException)
  }

  def 'should read search chandler from out-of-the-box solrconfig.xml file'() {
    given:
    def solrConfig = getClass().getResource("/solrconfig.xml").text

    when:
    def handlers = solrConfigXmlReader.read(solrConfig)

    then:
    handlers.searchHandler == "/select"
  }
}
