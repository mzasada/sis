package org.sis.connector.solr.api.messages

import spock.lang.Specification

class SearchHandlerResponseConverterTest extends Specification {

  SearchHandlerResponseConverter handlerResponseConverter = new SearchHandlerResponseConverter()

  def 'should convert successful response from search handler into readable format'() {
    given:
    def jsonResponse = """{"responseHeader":{"status":0.0,"QTime":13.0}}"""

    when:
    def converted = handlerResponseConverter.convert(jsonResponse)

    then:
    converted["result"] == "Success"
    converted["queryExecutionTime"] == "13 ms"
  }

  def 'should convert conflict response into readable format'() {
    given:
    def jsonResponse = """{"responseHeader":{"status":409.0,"QTime":73.1}}"""

    when:
    def converted = handlerResponseConverter.convert(jsonResponse)

    then:
    converted["result"] == "Conflict"
    converted["queryExecutionTime"] == "73 ms"
  }
}
