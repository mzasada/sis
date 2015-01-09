package org.sis.connector.solr.cluster.config

import spock.lang.Specification

class SolrConfigXmlReaderTest extends Specification {

  SolrConfigXmlReader solrConfigXmlReader = new SolrConfigXmlReader()

  def 'should read search handler with the smallest amount of defaults'() {
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
        <requestHandler name="/update" class="solr.UpdateRequestHandler" />
      </config>
        """

    when:
    def handlers = solrConfigXmlReader.read(config)

    then:
    handlers.searchHandler == "/simple"
  }

  def 'should read search handler with the smallest amount of search components'() {
    given:
    def config =
        """
      <?xml version="1.0" encoding="UTF-8" ?>
      <config>
        <requestHandler name="/get" class="solr.RealTimeGetHandler" />
        <requestHandler name="/query" class="solr.SearchHandler">
          <lst name="defaults">
              <str name="wt">json</str>
          </lst>
          <arr name="last-components">
            <str>spellcheck</str>
          </arr>
        </requestHandler>
        <requestHandler name="/simple" class="solr.SearchHandler">
          <lst name="defaults">
              <str name="wt">json</str>
          </lst>
        </requestHandler>
        <requestHandler name="/update" class="solr.UpdateRequestHandler" />
      </config>
        """

    when:
    def handlers = solrConfigXmlReader.read(config)

    then:
    handlers.searchHandler == "/simple"
  }

  def 'should require at least one search handler to be present'() {
    given:
    def config =
        """
      <?xml version="1.0" encoding="UTF-8" ?>
      <config>
        <requestHandler name="/get" class="solr.RealTimeGetHandler" />
        <requestHandler name="/update" class="solr.UpdateRequestHandler" />
      </config>
        """

    when:
    solrConfigXmlReader.read(config)

    then:
    thrown(InvalidConfigurationException)
  }

  def 'should require at least one update handler to be present'() {
    given:
    def config =
        """
      <?xml version="1.0" encoding="UTF-8" ?>
      <config>
        <requestHandler name="/query" class="solr.SearchHandler" />
        <requestHandler name="/get" class="solr.RealTimeGetHandler" />
      </config>
        """

    when:
    solrConfigXmlReader.read(config)

    then:
    thrown(InvalidConfigurationException)
  }


  def 'should read update handler from simplified config'() {
    given:
    def config =
        """
      <?xml version="1.0" encoding="UTF-8" ?>
      <config>
        <requestHandler name="/query" class="solr.SearchHandler" />
        <requestHandler name="/get" class="solr.RealTimeGetHandler" />
        <requestHandler name="/put" class="solr.UpdateRequestHandler" />
      </config>
        """

    when:
    def handlers = solrConfigXmlReader.read(config)

    then:
    handlers.updateHandler == "/put"
  }


  def 'should read all handlers from out-of-the-box solrconfig.xml file'() {
    given:
    def solrConfig = getClass().getResource("/solrconfig.xml").text

    when:
    def handlers = solrConfigXmlReader.read(solrConfig)

    then:
    handlers.searchHandler == "/select"
    handlers.updateHandler == "/update"
  }
}
