// Java 8+
/**
 * Listing 8.8 — ExecutorServiceBasics.java
 * Demonstrates: Thread pool creation, task submission, Future retrieval, and executor shutdown
 * Chapter 8: Concurrency Foundations and Coordination
 * Requires: Java 8+
 */
package chapter08;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ExecutorServiceBasics {

    private static final Logger log =
            Logger.getLogger(ExecutorServiceBasics.class.getName());

    // Record representing a notification task (data carrier)
    record NotificationTask(String customerId, String message) {}

    public static void main(String[] args) throws Exception {

        // Fixed pool — 4 threads serving all tasks
        // Threads are created once and reused across many tasks
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Submit a Callable — returns immediately with a Future handle
        // Execution happens asynchronously on a pool thread
        Future<String> result = executor.submit(() -> {
            // Simulate notification send latency
            Thread.sleep(100);
            return "Sent to customer-42";
        });

        // Submit a second task while the first is still running
        Future<String> result2 = executor.submit(() -> {
            Thread.sleep(50);
            return "Sent to customer-99";
        });

        // Other work can happen here while tasks execute on pool threads
        log.info("Tasks submitted — doing other work while pool threads execute");

        // Block until each result is available (get() waits if not done)
        String outcome1 = result.get();
        log.info("Outcome 1: " + outcome1);

        String outcome2 = result2.get();
        log.info("Outcome 2: " + outcome2);

        // Always shut down the executor to release pool threads
        executor.shutdown();

        // Wait up to 10 seconds for all tasks to complete
        boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);
        log.info("Executor terminated cleanly: " + finished);

        // Output:
        // INFO: Tasks submitted — doing other work while pool threads execute
        // INFO: Outcome 1: Sent to customer-42
        // INFO: Outcome 2: Sent to customer-99
        // INFO: Executor terminated cleanly: true
    }
}