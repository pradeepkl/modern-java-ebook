// Java 8+
/**
 * Listing 9.11 — DatabaseInteraction.java
 * Demonstrates: Correct Java time types for database column mapping
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+
 */
package chapter09;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.logging.Logger;

public class DatabaseInteraction {

    private static final Logger log = Logger.getLogger(DatabaseInteraction.class.getName());

    // Simulates an Order entity with correct date-time field types
    static class Order {

        private String orderId;

        // LocalDate maps to SQL DATE — no time component, date only
        private LocalDate deliveryDate;

        // Instant maps to SQL TIMESTAMP WITH TIME ZONE — stored as UTC
        private Instant createdAt;

        // OffsetDateTime — explicit offset stored with value, good for audit columns
        private OffsetDateTime lastModifiedAt;

        // LocalDateTime — WRONG for timestamps: no timezone, ambiguous when read back
        // private LocalDateTime createdAt; // DO NOT DO THIS

        Order(String orderId, LocalDate deliveryDate, Instant createdAt, OffsetDateTime lastModifiedAt) {
            this.orderId = orderId;
            this.deliveryDate = deliveryDate;
            this.createdAt = createdAt;
            this.lastModifiedAt = lastModifiedAt;
        }

        // Display createdAt in a user's local timezone — presentation concern only
        String createdAtInZone(ZoneId zone) {
            return createdAt.atZone(zone).toString();
        }
    }

    public static void main(String[] args) {
        LocalDate delivery = LocalDate.of(2024, 9, 15);          // SQL DATE
        Instant created = Instant.parse("2024-06-18T10:30:00Z"); // UTC instant
        OffsetDateTime modified = OffsetDateTime.of(             // explicit offset
                2024, 6, 18, 12, 30, 0, 0, ZoneOffset.ofHours(2));

        Order order = new Order("ORD-001", delivery, created, modified);

        log.info("Order ID      : " + order.orderId);
        log.info("Delivery Date : " + order.deliveryDate);       // SQL DATE
        log.info("Created At    : " + order.createdAt);          // UTC stored
        log.info("Last Modified : " + order.lastModifiedAt);     // offset preserved

        // Display UTC instant in different timezones — UI layer concern
        log.info("Created (NYC) : " + order.createdAtInZone(ZoneId.of("America/New_York")));
        log.info("Created (IST) : " + order.createdAtInZone(ZoneId.of("Asia/Kolkata")));

        // Output:
        // Order ID      : ORD-001
        // Delivery Date : 2024-09-15
        // Created At    : 2024-06-18T10:30:00Z
        // Last Modified : 2024-06-18T12:30:00+02:00
        // Created (NYC) : 2024-06-18T06:30-04:00[America/New_York]
        // Created (IST) : 2024-06-18T16:00+05:30[Asia/Kolkata]
    }
}