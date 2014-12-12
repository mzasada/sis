package org.sis.util.concurrent;

import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;

public class Futures {

  public static <T> CompletableFuture<T> completableFuture(ListenableFuture<T> future) {
    CompletableFuture<T> completableFuture = new CompletableFuture<>();
    future.addCallback(completableFuture::complete, completableFuture::completeExceptionally);
    return completableFuture;
  }
}
