// Java 25+
// Feature shown: unstructured CompletableFuture task spawning with no automatic cancellation of siblings, final in Java 8+
package chapter11;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Listing 11.10 — UnstructuredTaskProblem.java
 * Demonstrates: unstructured task spawning with CompletableFuture where a
 * failure in one branch does not automatically cancel sibling branches,
 * leaving orphaned work and wasted resources.
 * Chapter 11: Declarative and Structured Concurrency
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
public class UnstructuredTaskProblem {

    private static final Logger log =
            Logger.getLogger(UnstructuredTaskProblem.class.getName());

    void main() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Task A succeeds immediately
        CompletableFuture<String> taskA =
                CompletableFuture.supplyAsync(() ->
                        "result-A", executor);

        // Task B fails with a RuntimeException
        CompletableFuture<String> taskB =
                CompletableFuture.supplyAsync(() -> {
                    throw new RuntimeException("Task B failed");
                }, executor);

        // NOT IDEAL: Task A continues running even though
        // Task B has already failed. There is no
        // automatic cancellation of siblings.
        // The developer must coordinate manually.
        try {
            String a = taskA.get();   // may succeed
            String b = taskB.get();   // throws ExecutionException here
            log.info("Both succeeded: " + a + ", " + b);
        } catch (Exception e) {
            log.warning("One task failed: " + e.getMessage());
            // taskA result is now orphaned — wasted work
            // taskA is not cancelled; its result is simply discarded
            log.info("taskA completed but result discarded: "
                    + taskA.isDone());
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Output:
        // WARNING: One task failed: java.lang.RuntimeException: Task B failed
        // INFO: taskA completed but result discarded: true
    }
}