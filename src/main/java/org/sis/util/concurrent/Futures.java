package org.sis.util.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.Duration;
import java.util.concurrent.*;

import static java.util.function.Function.identity;

/**
 * @since 1.0
 */
public final class Futures {

  /**
   * Fixed size scheduler thread pool for handling asynchronous timeouts.
   * <p>
   * For more details read <a href="http://www.nurkiewicz.com/2014/12/asynchronous-timeouts-with.html">this</a> blog.
   */
  private static final ScheduledExecutorService TIMEOUT_SCHEDULER = Executors.newScheduledThreadPool(
      1, new ThreadFactoryBuilder()
          .setDaemon(true)
          .setNameFormat("failAfter-%d")
          .build());

  public static <T> CompletableFuture<T> completableFuture(ListenableFuture<T> future) {
    CompletableFuture<T> completableFuture = new CompletableFuture<>();
    future.addCallback(completableFuture::complete, completableFuture::completeExceptionally);
    return completableFuture;
  }

  public static <T> CompletableFuture<T> failAfter(Duration duration) {
    final CompletableFuture<T> promise = new CompletableFuture<>();
    TIMEOUT_SCHEDULER.schedule(() -> {
      final TimeoutException ex = new TimeoutException("Timeout after " + duration);
      return promise.completeExceptionally(ex);
    }, duration.toMillis(), TimeUnit.MILLISECONDS);
    return promise;
  }

  public static <T> CompletableFuture<T> within(CompletableFuture<T> future, Duration duration) {
    final CompletableFuture<T> timeout = failAfter(duration);
    return future.applyToEither(timeout, identity());
  }
}
