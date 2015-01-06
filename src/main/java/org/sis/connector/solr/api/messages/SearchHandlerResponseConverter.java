package org.sis.connector.solr.api.messages;

import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * Converts JSON responses from the search handlers into more verbose descriptions.
 *
 * @since 1.0
 */
public class SearchHandlerResponseConverter {
  private final ImmutableMap<Integer, String> errorCodes = ImmutableMap.<Integer, String>builder()
      .put(0, "Success")
      .put(400, "Bad request")
      .put(401, "Unauthorized")
      .put(404, "Forbidden")
      .put(409, "Conflict")
      .put(500, "Server error")
      .put(503, "Service unavailable")
      .build();

  public JSONObject convert(String searchHandlerResponse) {
    Object document = Configuration.defaultConfiguration().jsonProvider().parse(searchHandlerResponse);
    Map<String, Object> response = JsonPath.read(document, "$.responseHeader");

    return new JSONObject(ImmutableMap.builder()
        .put("result", getStatusMessage(response))
        .put("queryExecutionTime", getQueryExecutionTimeInMillis(response))
        .build());
  }

  private String getStatusMessage(Map<String, Object> response) {
    int errorCode = Double.valueOf(String.valueOf(response.get("status"))).intValue();
    return errorCodes.getOrDefault(errorCode, String.format("unknown error code - '%d'", errorCode));
  }

  private String getQueryExecutionTimeInMillis(Map<String, Object> response) {
    long timeInMillis = Double.valueOf(String.valueOf(response.get("QTime"))).intValue();
    return String.format("%d ms", timeInMillis);
  }
}
