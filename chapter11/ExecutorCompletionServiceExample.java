// Java 25+
// Feature shown: ExecutorCompletionService completion-order processing, final in Java 8+
package chapter11;

import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Listing 11.9 — ExecutorCompletionServiceExample.java
 * Demonstrates: ExecutorCompletionService processing results in completion order,
 * not submission order — whichever task finishes first is handled first.
 * Chapter 11: Declarative and Structured Concurrency
 * Requires: Java 8+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
public class ExecutorCompletionServiceExample {

    private static final Logger log =
            Logger.getLogger(
                    ExecutorCompletionServiceExample.class.getName());

    void main() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);
        // Wraps the executor; completed tasks are placed on an internal queue
        ExecutorCompletionService<String> completion =
                new ExecutorCompletionService<>(executor);

        List<String> orderIds = List.of(
                "ORD-001", "ORD-002", "ORD-003",
                "ORD-004", "ORD-005");

        // Submit all tasks — each simulates variable processing time
        for (String orderId : orderIds) {
            completion.submit(() -> {
                // Random delay so tasks finish out of submission order
                Thread.sleep((long) (Math.random() * 500));
                return "processed:" + orderId;
            });
        }

        // take() blocks until the next completed result is available.
        // Results arrive in completion order, not submission order.
        for (int i = 0; i < orderIds.size(); i++) {
            Future<String> completed = completion.take(); // blocks for next done
            log.info("Completed: " + completed.get());   // get() returns immediately
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Output (order varies by random sleep duration):
        // INFO: Completed: processed:ORD-003
        // INFO: Completed: processed:ORD-001
        // INFO: Completed: processed:ORD-005
        // INFO: Completed: processed:ORD-002
        // INFO: Completed: processed:ORD-004
    }
}