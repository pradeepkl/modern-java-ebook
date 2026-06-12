// Java 8+
/**
 * Listing 9.8 — ChronoUnitAndTimeUnit.java
 * Demonstrates: ChronoUnit for java.time arithmetic vs TimeUnit for concurrency APIs
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+
 */
package chapter09;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ChronoUnitAndTimeUnit {

    private static final Logger log = Logger.getLogger(ChronoUnitAndTimeUnit.class.getName());

    public static void main(String[] args) throws Exception {

        // ChronoUnit — java.time arithmetic
        LocalDate today = LocalDate.now();
        LocalDate nextQuarter = today.plus(3, ChronoUnit.MONTHS); // add 3 months
        LocalDate nextWeek    = today.plus(1, ChronoUnit.WEEKS);  // add 1 week
        log.info("Today: " + today + ", Next quarter: " + nextQuarter + ", Next week: " + nextWeek);

        // ChronoUnit.between — calculate elapsed time
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end   = LocalDate.of(2024, 6, 18);

        long daysBetween   = ChronoUnit.DAYS.between(start, end);   // 169
        long monthsBetween = ChronoUnit.MONTHS.between(start, end); // 5
        long weeksBetween  = ChronoUnit.WEEKS.between(start, end);  // 24
        log.info("Days: " + daysBetween + ", Months: " + monthsBetween + ", Weeks: " + weeksBetween);

        // ChronoUnit with Instant
        Instant eventA = Instant.now();
        Instant eventB = eventA.plus(90, ChronoUnit.MINUTES);       // add 90 minutes
        long minutesBetween = ChronoUnit.MINUTES.between(eventA, eventB); // 90
        log.info("Minutes between events: " + minutesBetween);

        // TimeUnit — concurrency timeouts (contrast with ChronoUnit)
        ExecutorService executor = Executors.newFixedThreadPool(4);

        Future<String> result = executor.submit(() -> "result");
        String value = result.get(5, TimeUnit.SECONDS); // timeout with TimeUnit
        log.info("Future result: " + value);

        executor.shutdown();
        boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS); // TimeUnit for timeout
        log.info("Executor finished: " + finished);

        // Converting between TimeUnit values
        long millisFromSeconds  = TimeUnit.SECONDS.toMillis(30); // 30000
        long secondsFromMinutes = TimeUnit.MINUTES.toSeconds(2); // 120
        log.info("30s in millis: " + millisFromSeconds + ", 2min in seconds: " + secondsFromMinutes);

        // Output:
        // Today: 2024-06-18, Next quarter: 2024-09-18, Next week: 2024-06-25
        // Days: 169, Months: 5, Weeks: 24
        // Minutes between events: 90
        // Future result: result
        // Executor finished: true
        // 30s in millis: 30000, 2min in seconds: 120
    }
}