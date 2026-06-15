// Java 25+
// Feature shown: ChronoUnit for date arithmetic, TimeUnit for concurrency, final in Java 8+

/**
 * Listing 9.8 — ChronoUnitAndTimeUnit.java
 * Demonstrates: ChronoUnit (java.time arithmetic) vs TimeUnit (concurrency timeouts)
 * Chapter 9: Modern Date and Time
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter09;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class ChronoUnitAndTimeUnit {

    private static final Logger LOG =
            Logger.getLogger(ChronoUnitAndTimeUnit.class.getName());

    void main() {
        // ChronoUnit — java.time arithmetic
        LocalDate today = LocalDate.now();
        LocalDate nextQuarter = today.plus(3, ChronoUnit.MONTHS);  // 3 months ahead
        LocalDate nextWeek    = today.plus(1, ChronoUnit.WEEKS);   // 1 week ahead
        LOG.info("Next quarter: " + nextQuarter + ", next week: " + nextWeek);

        // ChronoUnit.between — calculate elapsed time
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end   = LocalDate.of(2024, 6, 18);

        long daysBetween    = ChronoUnit.DAYS.between(start, end);    // 169
        long monthsBetween  = ChronoUnit.MONTHS.between(start, end);  // 5
        long weeksBetween   = ChronoUnit.WEEKS.between(start, end);   // 24
        LOG.info("Days: " + daysBetween + ", months: " + monthsBetween
                + ", weeks: " + weeksBetween);

        // ChronoUnit with Instant
        Instant eventA = Instant.now();
        Instant eventB = eventA.plus(90, ChronoUnit.MINUTES);          // 90 min later
        long minutesBetween = ChronoUnit.MINUTES.between(eventA, eventB); // 90
        LOG.info("Minutes between instants: " + minutesBetween);

        // TimeUnit — concurrency timeouts (shown here for contrast)
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Future<String> result = executor.submit(() -> "result");

        // shutdown() stops accepting new tasks; submitted tasks complete
        executor.shutdown();
        try {
            // TimeUnit specifies the timeout unit for concurrency
            boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);
            LOG.info("Executor finished within timeout: " + finished);

            // get() declares checked exceptions — always handle them
            String value = result.get(5, TimeUnit.SECONDS);
            LOG.info("Task result: " + value);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOG.warning("Task failure or timeout: " + e.getMessage());
        }

        // Converting between TimeUnit values
        long millisFromSeconds  = TimeUnit.SECONDS.toMillis(30);  // 30000
        long secondsFromMinutes = TimeUnit.MINUTES.toSeconds(2);  // 120
        LOG.info("30s in ms: " + millisFromSeconds
                + ", 2min in s: " + secondsFromMinutes);

        // Output:
        // Next quarter: 2025-04-..., next week: 2025-01-...
        // Days: 169, months: 5, weeks: 24
        // Minutes between instants: 90
        // Executor finished within timeout: true
        // Task result: result
        // 30s in ms: 30000, 2min in s: 120
    }
}