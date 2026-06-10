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
                    log.info("Task A running...");
                    return "result-A";
                }, executor);

        // Task B fails — simulates a downstream service error
        CompletableFuture<String> taskB =
                CompletableFuture.supplyAsync(() -> {
                    // ❌ This exception does not cancel Task A automatically
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
            // Task A result is now orphaned — wasted work
            log.warning("One task failed: " + e.getMessage());
            log.warning("Task A result orphaned — no automatic sibling cancellation");
        }

        // Manual cancellation required — structured concurrency solves this
        taskA.cancel(true);

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Output:
        // INFO: Task A running...
        // WARNING: One task failed: java.lang.RuntimeException: Task B failed
        // WARNING: Task A result orphaned — no automatic sibling cancellation
    }
}