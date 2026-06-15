// Java 25+
// Feature shown: Duration and Period, final in Java 8+

/**
 * Listing 9.7 — DurationAndPeriod.java
 * Demonstrates: Duration for machine elapsed time and Period for calendar elapsed time
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter09;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.logging.Logger;

public class DurationAndPeriod {

    private static final Logger LOG = Logger.getLogger(DurationAndPeriod.class.getName());

    void main() {
        // Duration — machine elapsed time (works with Instant)
        Instant start = Instant.now();
        Instant end = start.plusMillis(1500); // simulate 1.5 seconds elapsed

        Duration elapsed = Duration.between(start, end);
        long millis  = elapsed.toMillis();   // 1500
        long seconds = elapsed.toSeconds();  // 1
        long nanos   = elapsed.toNanos();    // 1_500_000_000

        LOG.info("Elapsed millis:  " + millis);
        LOG.info("Elapsed seconds: " + seconds);
        LOG.info("Elapsed nanos:   " + nanos);

        // Duration arithmetic — combine two durations
        Duration timeout    = Duration.ofSeconds(30);
        Duration retryDelay = Duration.ofMillis(500);
        Duration totalWait  = timeout.plus(retryDelay); // 30.5 seconds

        LOG.info("Total wait ms: " + totalWait.toMillis()); // 30500

        // Period — calendar elapsed time (works with LocalDate)
        LocalDate subscriptionStart = LocalDate.of(2024, 1, 15);
        LocalDate subscriptionEnd   = LocalDate.of(2024, 7, 15);

        Period subscriptionLength = Period.between(subscriptionStart, subscriptionEnd);
        int months = subscriptionLength.getMonths(); // 6

        LOG.info("Subscription months: " + months);

        // Period arithmetic — advance a date by a calendar period
        LocalDate renewalDate = subscriptionEnd.plus(Period.ofMonths(12));

        LOG.info("Renewal date: " + renewalDate); // 2025-07-15

        // Key distinction:
        // Duration.ofDays(30) is always 30 x 24 hours (fixed machine time)
        // Period.ofMonths(1) varies — Jan has 31 days, Feb 28 or 29, Jun 30
        // Use Period when calendar semantics matter
        LOG.info("Duration 30 days in hours: " + Duration.ofDays(30).toHours()); // 720
        LOG.info("Period 1 month days (from Jan 15): " +
                LocalDate.of(2024, 1, 15).plus(Period.ofMonths(1))); // 2024-02-15

        // Output:
        // Elapsed millis:  1500
        // Elapsed seconds: 1
        // Elapsed nanos:   1500000000
        // Total wait ms: 30500
        // Subscription months: 6
        // Renewal date: 2025-07-15
        // Duration 30 days in hours: 720
        // Period 1 month days (from Jan 15): 2024-02-15
    }
}