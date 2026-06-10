// Java 8+
package chapter09;

import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Listing 9.9 — ExecutorCompletionServiceExample.java
 * Demonstrates: Processing task results in completion order, not submission order
 * Chapter 9: Declarative and Structured Concurrency
 * Requires: Java 8+
 */
public class ExecutorCompletionServiceExample {

    private static final Logger log =
            Logger.getLogger(
                ExecutorCompletionServiceExample.class.getName());

    public static void main(String[] args) throws Exception {

        // Fixed thread pool to process orders concurrently
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Wraps executor — completed tasks are queued internally
        ExecutorCompletionService<String> completion =
                new ExecutorCompletionService<>(executor);

        List<String> orderIds = List.of(
                "ORD-001", "ORD-002", "ORD-003",
                "ORD-004", "ORD-005");

        // Submit all tasks — each simulates variable processing time
        for (String orderId : orderIds) {
            completion.submit(() -> {
                // Simulate variable processing time per order
                Thread.sleep((long) (Math.random() * 500));
                return "processed:" + orderId;
            });
        }

        // Process results as they complete —
        // not in submission order, but in completion order.
        // take() blocks until the next completed result is available.
        for (int i = 0; i < orderIds.size(); i++) {
            Future<String> completed = completion.take(); // blocks for next done
            log.info("Completed: " + completed.get());   // get() returns immediately
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Output (order varies by completion time, not submission order):
        // INFO: Completed: processed:ORD-003
        // INFO: Completed: processed:ORD-001
        // INFO: Completed: processed:ORD-005
        // INFO: Completed: processed:ORD-002
        // INFO: Completed: processed:ORD-004
    }
}