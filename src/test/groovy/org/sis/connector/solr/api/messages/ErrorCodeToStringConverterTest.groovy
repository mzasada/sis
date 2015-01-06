package org.sis.connector.solr.api.messages

import spock.lang.Specification
import spock.lang.Unroll

class ErrorCodeToStringConverterTest extends Specification {

  ErrorCodeToStringConverter codeToStringConverter = new ErrorCodeToStringConverter()

  @Unroll
  def 'should return message for existing error code'(int errorCode) {
    expect:
    def message = codeToStringConverter.getErrorCodeMessage(errorCode)

    !message?.isEmpty()
    !message.contains("Unknown")

    where:
    errorCode << [0, 400, 401, 404, 409, 500, 503]
  }
}
