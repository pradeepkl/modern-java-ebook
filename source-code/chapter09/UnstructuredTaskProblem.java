// Java 8+
/**
 * Listing 9.10 — UnstructuredTaskProblem.java
 * Demonstrates: Unstructured task spawning problem with CompletableFuture
 * Chapter 9: Declarative and Structured Concurrency
 * Requires: Java 8+
 */
package chapter09;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class UnstructuredTaskProblem {

    private static final Logger log =
            Logger.getLogger(UnstructuredTaskProblem.class.getName());

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Task A succeeds — simulates normal work
        CompletableFuture<String> taskA =
                CompletableFuture.supplyAsync(() -> {
                    log.info("Task A running on: " + Thread.currentThread().getName());
                    return "result-A";
                }, executor);

        // Task B fails — simulates a downstream service error
        CompletableFuture<String> taskB =
                CompletableFuture.supplyAsync(() -> {
                    log.info("Task B running on: " + Thread.currentThread().getName());
                    // Deliberately throw to show sibling is NOT cancelled
                    throw new RuntimeException("Task B failed");
                }, executor);

        // ❌ Task A continues running even though Task B has already failed.
        // There is no automatic cancellation of siblings.
        // The developer must coordinate manually.
        try {
            String a = taskA.get();   // may succeed
            String b = taskB.get();   // throws ExecutionException here
            log.info("Both succeeded: " + a + ", " + b);
        } catch (Exception e) {
            // taskA result is now orphaned — wasted work
            log.warning("One task failed: " + e.getMessage());
            log.warning("taskA completed: " + taskA.isDone()
                    + " — result orphaned, no automatic cancellation occurred");
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Output:
        // INFO: Task A running on: pool-1-thread-1
        // INFO: Task B running on: pool-1-thread-2
        // WARNING: One task failed: java.lang.RuntimeException: Task B failed
        // WARNING: taskA completed: true — result orphaned, no automatic cancellation occurred
    }
}