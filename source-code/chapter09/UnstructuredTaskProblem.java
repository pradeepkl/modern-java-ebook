// Java 8+
package chapter09;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Listing 9.10 — UnstructuredTaskProblem.java
 * Demonstrates: The problem with unstructured task spawning using CompletableFuture.
 *               When Task B fails, Task A continues running with no automatic
 *               cancellation of sibling tasks — a core limitation of unstructured
 *               concurrency that Structured Concurrency addresses.
 * Chapter 9: Declarative and Structured Concurrency
 * Requires: Java 8+
 */
public class UnstructuredTaskProblem {

    private static final Logger log =
            Logger.getLogger(UnstructuredTaskProblem.class.getName());

    public static void main(String[] args) throws Exception {

        ExecutorService executor =
                Executors.newFixedThreadPool(4);

        // Task A succeeds — simulates normal background work
        CompletableFuture<String> taskA =
                CompletableFuture.supplyAsync(() -> {
                    log.info("Task A running on: " + Thread.currentThread().getName());
                    return "result-A";
                }, executor);

        // Task B fails — simulates a downstream service error
        CompletableFuture<String> taskB =
                CompletableFuture.supplyAsync(() -> {
                    throw new RuntimeException("Task B failed");
                }, executor);

        // ❌ Task A continues running even though Task B has already failed.
        // There is no automatic cancellation of siblings.
        // The developer must coordinate cancellation manually.
        try {
            String a = taskA.get();       // may succeed
            String b = taskB.get();       // throws ExecutionException here
            log.info("Both results: " + a + ", " + b);
        } catch (Exception e) {
            log.warning("One task failed: " + e.getMessage());
            // taskA result is now orphaned — wasted work, no cleanup guarantee
            log.warning("taskA done=" + taskA.isDone() + " cancelled=" + taskA.isCancelled());
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Output:
        // INFO: Task A running on: pool-1-thread-1
        // WARNING: One task failed: java.lang.RuntimeException: Task B failed
        // WARNING: taskA done=true cancelled=false
    }
}