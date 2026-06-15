// Java 25+
// Feature shown: Clock abstraction for testable time-dependent code, final in Java 8+
/**
 * Listing 9.10 — ClockAbstraction.java
 * Demonstrates: Clock abstraction for testable, deterministic time-dependent code
 * Chapter 9: Modern Date and Time
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter09;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.logging.Logger;

public class ClockAbstraction {

    private static final Logger log =
            Logger.getLogger(ClockAbstraction.class.getName());

    record Subscription(String customerId,
            LocalDate startDate,
            LocalDate endDate) {

        boolean isActive(Clock clock) {
            LocalDate today = LocalDate.now(clock); // inject clock, not system time
            return !today.isBefore(startDate) && !today.isAfter(endDate);
        }

        boolean expiresWithinDays(int days, Clock clock) {
            LocalDate today = LocalDate.now(clock);
            LocalDate threshold = today.plusDays(days);
            return endDate.isBefore(threshold);
        }
    }

    void main() {
        // Production usage — real system clock, non-deterministic
        Clock productionClock = Clock.systemDefaultZone();
        Subscription prod = new Subscription(
                "C001",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2099, 12, 31));
        log.info("Production active: " + prod.isActive(productionClock));

        // Test usage — fixed clock, fully deterministic
        Clock fixedClock = Clock.fixed(
                Instant.parse("2024-06-18T00:00:00Z"),
                ZoneId.of("UTC")); // clock frozen at 2024-06-18

        Subscription sub = new Subscription(
                "C001",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));

        boolean active = sub.isActive(fixedClock);
        // Always true for this fixed date — deterministic
        log.info("Active on 2024-06-18: " + active);

        boolean expiresSoon = sub.expiresWithinDays(30, fixedClock);
        // Always false — Dec 31 is more than 30 days from Jun 18
        log.info("Expires within 30 days: " + expiresSoon);

        boolean expiresLate = sub.expiresWithinDays(200, fixedClock);
        // True — Dec 31 is within 200 days of Jun 18
        log.info("Expires within 200 days: " + expiresLate);

        // Output:
        // Production active: true
        // Active on 2024-06-18: true
        // Expires within 30 days: false
        // Expires within 200 days: true
    }
}