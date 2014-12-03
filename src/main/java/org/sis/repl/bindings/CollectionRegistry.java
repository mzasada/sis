package org.sis.repl.bindings;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.sis.ipc.events.ClusterStatusUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Component
public class CollectionRegistry {

  private static final Logger LOGGER = LoggerFactory.getLogger(CollectionRegistry.class);

  private final Map<String, CollectionOperations> collections = new TreeMap<>();

  @Autowired
  public CollectionRegistry(EventBus eventBus) {
    eventBus.register(this);
  }

  @Subscribe
  public void handle(ClusterStatusUpdateEvent event) {
    updateCollection(event.getCollectionNames());
  }

  public Map<String, CollectionOperations> getCurrentCollectionsView() {
    return collections;
  }

  private void updateCollection(Set<String> collectionNames) {
    collections.clear();
    collectionNames.forEach(
        name -> collections.put(name, new CollectionOperations(name)));
  }
}
