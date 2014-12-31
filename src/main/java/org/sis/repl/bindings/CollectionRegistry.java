package org.sis.repl.bindings;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingSortedMap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.sis.ipc.events.ClusterStatusUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

@Component
public class CollectionRegistry {

  private final SortedMap<String, OperationsFacade> existingCollections = new TreeMap<>();
  private final ForwardingMap<String, OperationsFacade> collectionsView = new ForwardingSortedMap<String, OperationsFacade>() {

    @Override
    public OperationsFacade get(Object key) {
      return existingCollections.getOrDefault(key, new NewCollectionOperations(String.valueOf(key)));
    }

    @Override
    protected SortedMap<String, OperationsFacade> delegate() {
      return existingCollections;
    }
  };

  @Autowired
  public CollectionRegistry(EventBus eventBus) {
    eventBus.register(this);
  }

  @Subscribe
  public void handle(ClusterStatusUpdateEvent event) {
    updateCollection(event.getCollectionNames());
  }

  public Map<String, OperationsFacade> getCurrentCollectionsView() {
    return collectionsView;
  }

  private void updateCollection(Set<String> collectionNames) {
    existingCollections.clear();
    collectionNames.forEach(
        name -> existingCollections.put(name, new ExistingCollectionOperations(name)));
  }
}
