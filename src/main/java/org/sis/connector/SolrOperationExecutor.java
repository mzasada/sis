package org.sis.connector;

import com.google.common.collect.ImmutableMap;
import org.json.simple.JSONObject;
import org.sis.repl.bindings.operations.CreateCollectionOperation;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
public class SolrOperationExecutor implements OperationExecutor {

  @Override
  public Future<String> submit(CreateCollectionOperation operation) {
    //TODO: do something?
    JSONObject result = new JSONObject(ImmutableMap.of("collection", "created"));
    return CompletableFuture.completedFuture(result.toJSONString());
  }
}
