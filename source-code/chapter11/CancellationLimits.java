// Java 25+
// Feature shown: CompletableFuture.cancel() limitations, final in Java 8+

/**
 * Listing 11.8 — CancellationLimits.java
 * Demonstrates: CompletableFuture.cancel() marks the future cancelled but does
 * not interrupt the underlying thread — the task continues running to completion.
 * Chapter 11: Declarative and Structured Concurrency
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter11;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CancellationLimits {

    private static final Logger log =
            Logger.getLogger(CancellationLimits.class.getName());

    void main() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Task sleeps 3 seconds — cancel() will not stop it
        CompletableFuture<String> future =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        log.info("Task started — running...");
                        Thread.sleep(3000);
                        log.info("Task completed normally"); // still prints
                        return "result";
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return "interrupted";
                    }
                }, executor);

        Thread.sleep(100); // let the task start

        // Marks the future as cancelled.
        // Does NOT interrupt the thread running the task.
        // The task continues executing in the background.
        boolean cancelled = future.cancel(true);
        log.info("Cancelled: " + cancelled);

        try {
            future.get(); // throws CancellationException immediately
        } catch (CancellationException e) {
            log.warning("Future was cancelled — but task may still be running");
        }

        // The task log line "Task completed normally" will still appear
        // ~3 seconds after cancel() because the underlying thread was not stopped.
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Output:
        // INFO: Task started — running...
        // INFO: Cancelled: true
        // WARNING: Future was cancelled — but task may still be running
        // INFO: Task completed normally
    }
}