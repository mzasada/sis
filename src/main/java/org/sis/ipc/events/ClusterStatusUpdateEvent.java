package org.sis.ipc.events;

import java.util.Set;

public class ClusterStatusUpdateEvent {
  private final Set<String> collectionNames;

  public ClusterStatusUpdateEvent(Set<String> collectionNames) {
    this.collectionNames = collectionNames;
  }

  public Set<String> getCollectionNames() {
    return collectionNames;
  }
}
