// Java 8+
/**
 * Listing 9.10 — ClockAbstraction.java
 * Demonstrates: Using Clock abstraction for deterministic, testable date logic
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+
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
            LocalDate today = LocalDate.now(clock); // inject clock, not system
            return !today.isBefore(startDate) && !today.isAfter(endDate);
        }

        boolean expiresWithinDays(int days, Clock clock) {
            LocalDate today = LocalDate.now(clock);
            LocalDate threshold = today.plusDays(days);
            return endDate.isBefore(threshold); // true if expiry is soon
        }
    }

    // Production usage — real system clock
    public static void checkSubscription(Subscription sub) {
        Clock productionClock = Clock.systemDefaultZone(); // real wall clock
        log.info("Active (production clock): " + sub.isActive(productionClock));
    }

    // Test usage — fixed clock, fully deterministic
    public static void testSubscription() {
        // Fix the clock at a known date — always 2024-06-18
        Clock fixedClock = Clock.fixed(
                Instant.parse("2024-06-18T00:00:00Z"),
                ZoneId.of("UTC"));

        Subscription sub = new Subscription(
                "C001",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));

        boolean active = sub.isActive(fixedClock);
        log.info("Active (fixed clock 2024-06-18): " + active); // always true

        boolean expiresSoon = sub.expiresWithinDays(30, fixedClock);
        log.info("Expires within 30 days: " + expiresSoon); // always false
    }

    public static void main(String[] args) {
        Subscription sub = new Subscription(
                "C001",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));

        checkSubscription(sub);  // uses real system clock
        testSubscription();      // uses fixed deterministic clock

        // Output:
        // Active (production clock): false  (depends on current date)
        // Active (fixed clock 2024-06-18): true
        // Expires within 30 days: false
    }
}