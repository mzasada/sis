package org.sis.connector.solr.cluster.config;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.sis.connector.solr.cluster.SolrNode;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Reads {@code /clusterstate.json} file from a ZooKeeper ensemble, which represents current state of a Solr cluster.
 *
 * @since 1.0
 */
public class SolrClusterStateReader {

  /**
   * @param json {@code /clusterstate.json} file content from a ZooKeeper ensable.
   * @return multimap of collection name and Solr nodes across which it is distributed
   */
  public Multimap<String, SolrNode> readClusterState(String json) {
    Multimap<String, SolrNode> cluster = HashMultimap.create();
    Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
    Map<String, ?> collections = JsonPath.read(document, "$");
    collections.keySet().forEach(k -> cluster.putAll(k, readAllReplicas(document, k)));
    return cluster;
  }

  private List<SolrNode> readAllReplicas(Object document, String collectionName) {
    List<Map<String, Map<String, String>>> replicas = JsonPath.read(document, String.format("$.%s.shards.*.replicas", collectionName));
    return replicas.stream()
        .map(Map::values)
        .flatMap(Collection::stream)
        .map(node -> new SolrNode(node.get("base_url"), Boolean.valueOf(node.get("leader"))))
        .collect(toList());
  }

}
