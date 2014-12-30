package org.sis.connector.solr.api;

import com.google.common.net.MediaType;
import org.sis.connector.solr.cluster.SolrNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

import static com.google.common.net.MediaType.XML_UTF_8;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.sis.util.concurrent.Futures.completableFuture;

@Component
public class SolrFileServiceImpl implements SolrFileService {

  private final AsyncRestOperations asyncRestOperations;

  @Autowired
  public SolrFileServiceImpl(AsyncRestOperations asyncRestOperations) {
    this.asyncRestOperations = asyncRestOperations;
  }

  @Override
  public CompletableFuture<String> fetchFile(SolrNode node, String collection, String filename, MediaType mediaType) {
    UriComponents uriComponents = UriComponentsBuilder
        .fromHttpUrl(getFilesApiEndpoint(node.getHost(), collection))
        .queryParam("file", filename)
        .queryParam("contentType", mediaType.withoutParameters().toString())
        .queryParam("charset", mediaType.charset().or(UTF_8))
        .build();

    return completableFuture(asyncRestOperations.getForEntity(uriComponents.toUri(), String.class))
        .thenApply(HttpEntity::getBody);
  }

  @Override
  public CompletableFuture<String> fetchSolrConfigXml(SolrNode node, String collection) {
    return fetchFile(node, collection, "solrconfig.xml", XML_UTF_8);
  }

  private String getFilesApiEndpoint(String solrHost, String collectionName) {
    return String.format("%s/%s/admin/file", solrHost, collectionName);
  }
}
