// Java 25+
// Feature shown: java.time types with JPA (LocalDate, Instant, OffsetDateTime), final in Java 8+

package chapter09;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.logging.Logger;

/**
 * Listing 9.11 — DatabaseInteraction.java
 * Demonstrates: Correct java.time types for database persistence columns.
 * LocalDate maps to SQL DATE, Instant maps to SQL TIMESTAMP WITH TIME ZONE,
 * OffsetDateTime preserves the exact UTC offset for audit columns.
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+ for java.time types; void main() requires Java 25+
 * (compiled with --enable-preview --release 21 for the void main() instance
 * main method)
 */
public class DatabaseInteraction {

    private static final Logger log =
            Logger.getLogger(DatabaseInteraction.class.getName());

    // Simulates a JPA entity — no jakarta.persistence on classpath needed
    static class Order {

        private String orderId;

        // LocalDate maps to SQL DATE — no time component, date only
        private LocalDate deliveryDate;

        // Instant maps to SQL TIMESTAMP WITH TIME ZONE — stored as UTC
        private Instant createdAt;

        // OffsetDateTime — explicit offset stored with value
        // Good for audit columns where the exact offset matters
        private OffsetDateTime lastModifiedAt;

        // LocalDateTime — WRONG for timestamps: no timezone, ambiguous on read
        // private LocalDateTime createdAt; // DO NOT DO THIS

        Order(String orderId,
              LocalDate deliveryDate,
              Instant createdAt,
              OffsetDateTime lastModifiedAt) {
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
        // LocalDate — date only, no time, no timezone
        LocalDate delivery = LocalDate.of(2024, 7, 15);

        // Instant — always UTC, use for machine-generated timestamps
        Instant created = Instant.parse("2024-06-18T09:30:00Z");

        // OffsetDateTime — UTC offset explicit, ideal for audit trails
        OffsetDateTime modified = OffsetDateTime.of(
                2024, 6, 18, 11, 45, 0, 0, ZoneOffset.ofHours(2));

        Order order = new Order("ORD-001", delivery, created, modified);

        log.info("Persisted order: " + order);
        log.info("deliveryDate type: LocalDate  -> SQL DATE");
        log.info("createdAt type:    Instant    -> SQL TIMESTAMP WITH TIME ZONE (UTC)");
        log.info("lastModifiedAt:    OffsetDateTime -> SQL TIMESTAMP WITH TIME ZONE");

        // Output:
        // Persisted order: Order{id=ORD-001, deliveryDate=2024-07-15,
        //   createdAt=2024-06-18T09:30:00Z, lastModifiedAt=2024-06-18T11:45+02:00}
        // deliveryDate type: LocalDate  -> SQL DATE
        // createdAt type:    Instant    -> SQL TIMESTAMP WITH TIME ZONE (UTC)
        // lastModifiedAt:    OffsetDateTime -> SQL TIMESTAMP WITH TIME ZONE
    }
}