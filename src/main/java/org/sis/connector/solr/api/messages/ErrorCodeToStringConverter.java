package org.sis.connector.solr.api.messages;

import com.google.common.collect.ImmutableMap;

/**
 * Mapping between Solr error codes returned by search handlers and more meaningful messages.
 *
 * @since 1.0
 */
public final class ErrorCodeToStringConverter {
  private final ImmutableMap<Integer, String> errorCodes = ImmutableMap.<Integer, String>builder()
      .put(0, "Success")
      .put(400, "Bad request")
      .put(401, "Unauthorized")
      .put(404, "Forbidden")
      .put(409, "Conflict")
      .put(500, "Server error")
      .put(503, "Service unavailable")
      .build();

  public String getErrorCodeMessage(int errorCode) {
    return errorCodes.getOrDefault(errorCode, String.format("Unknown error code - '%d'", errorCode));
  }
}
