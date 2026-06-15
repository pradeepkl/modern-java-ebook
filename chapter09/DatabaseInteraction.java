// Java 25+
// Feature shown: java.time types with JPA (LocalDate, Instant, OffsetDateTime), final in Java 8+

package chapter09;

/**
 * Listing 9.11 — DatabaseInteraction.java
 * Demonstrates: Correct java.time type choices for database columns:
 *   LocalDate for date-only, Instant for UTC timestamps,
 *   OffsetDateTime for audit columns with explicit offset.
 *   Also shows why LocalDateTime is wrong for stored timestamps.
 * Chapter 9: Modern Date and Time
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.logging.Logger;

public class DatabaseInteraction {

    private static final Logger log =
            Logger.getLogger(DatabaseInteraction.class.getName());

    // Simulates a JPA entity — annotations omitted (no jakarta.persistence on classpath)
    static class Order {

        private String orderId;

        // LocalDate maps to SQL DATE — no time component, date only
        private LocalDate deliveryDate;

        // Instant maps to SQL TIMESTAMP WITH TIME ZONE — stored as UTC
        private Instant createdAt;

        // OffsetDateTime — explicit offset stored with value
        // Good for audit columns where the exact offset matters
        private OffsetDateTime lastModifiedAt;

        // LocalDateTime would be WRONG here — no timezone, ambiguous when read back
        // private LocalDateTime createdAt; // DO NOT DO THIS

        Order(String orderId, LocalDate deliveryDate,
              Instant createdAt, OffsetDateTime lastModifiedAt) {
            this.orderId = orderId;
            this.deliveryDate = deliveryDate;
            this.createdAt = createdAt;
            this.lastModifiedAt = lastModifiedAt;
        }

        @Override
        public String toString() {
            return "Order{id=" + orderId
                    + ", deliveryDate=" + deliveryDate
                    + ", createdAt=" + createdAt
                    + ", lastModifiedAt=" + lastModifiedAt + "}";
        }
    }

    void main() {
        // LocalDate — date only, no time, maps to SQL DATE
        LocalDate delivery = LocalDate.of(2024, 7, 15);

        // Instant — UTC moment, maps to SQL TIMESTAMP WITH TIME ZONE
        Instant created = Instant.parse("2024-06-18T09:30:00Z");

        // OffsetDateTime — UTC offset preserved, good for audit trails
        OffsetDateTime modified = OffsetDateTime.of(
                2024, 6, 18, 11, 45, 0, 0, ZoneOffset.ofHours(2));

        Order order = new Order("ORD-001", delivery, created, modified);

        log.info("Persisted order: " + order);
        log.info("Delivery date (SQL DATE): " + order.deliveryDate);
        log.info("Created at UTC (SQL TIMESTAMP): " + order.createdAt);
        log.info("Last modified with offset: " + order.lastModifiedAt);

        // Output:
        // Persisted order: Order{id=ORD-001, deliveryDate=2024-07-15,
        //   createdAt=2024-06-18T09:30:00Z,
        //   lastModifiedAt=2024-06-18T11:45:00+02:00}
        // Delivery date (SQL DATE): 2024-07-15
        // Created at UTC (SQL TIMESTAMP): 2024-06-18T09:30:00Z
        // Last modified with offset: 2024-06-18T11:45:00+02:00
    }
}