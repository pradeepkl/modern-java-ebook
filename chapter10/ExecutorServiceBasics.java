// Java 25+
// Feature shown: ExecutorService and Future task submission, final in Java 8+

/**
 * Listing 10.8 — ExecutorServiceBasics.java
 * Demonstrates: ExecutorService fixed thread pool and Future-based task submission
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter10;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class ExecutorServiceBasics {

    private static final Logger log =
            Logger.getLogger(ExecutorServiceBasics.class.getName());

    // Record representing a notification task payload
    record NotificationTask(String customerId, String message) {}

    void main() throws InterruptedException {

        // Fixed pool — 4 threads serving all submitted tasks
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Submit a Callable — returns immediately with a Future handle
        // Execution happens on a pool thread, not the calling thread
        Future<String> result = executor.submit(() -> {
            Thread.sleep(100); // Simulate notification send latency
            return "Sent to customer-42";
        });

        // Other work can proceed here while the task runs on a pool thread
        log.info("Task submitted; continuing other work on calling thread");

        // Block until the task result is available, with a timeout guard
        // Call get() before shutdown() — retrieve results first, then clean up
        try {
            String outcome = result.get(5, TimeUnit.SECONDS);
            log.info("Outcome: " + outcome);
        } catch (ExecutionException e) {
            log.warning("Task threw an exception: " + e.getMessage());
        } catch (TimeoutException e) {
            log.warning("Task did not complete within timeout: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warning("Interrupted while waiting for result: " + e.getMessage());
        }

        // shutdown() stops accepting new tasks but lets in-flight tasks finish
        executor.shutdown();
        // awaitTermination() blocks until all tasks complete or timeout elapses
        boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);
        log.info("Executor terminated cleanly: " + finished);

        // Output:
        // INFO: Task submitted; continuing other work on calling thread
        // INFO: Outcome: Sent to customer-42
        // INFO: Executor terminated cleanly: true
    }
}