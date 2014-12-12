package org.sis.connector.solr.api;

import com.google.common.net.MediaType;
import org.sis.connector.solr.cluster.SolrNode;

import java.util.concurrent.CompletableFuture;

public interface SolrFileService {

  CompletableFuture<String> fetchFile(SolrNode node, String collection, String filename, MediaType mediaType);

  CompletableFuture<String> fetchSolrConfigXml(SolrNode node, String collection);
}
