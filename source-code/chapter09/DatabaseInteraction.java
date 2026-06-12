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

    private static final Logger log = Logger.getLogger(
            DatabaseInteraction.class.getName());

    // Simulates a JPA entity — without actual JPA annotations
    static class Order {

        private String orderId;

        // Maps to SQL DATE — date only, no time component
        private LocalDate deliveryDate;

        // Maps to SQL TIMESTAMP WITH TIME ZONE — stored as UTC
        private Instant createdAt;

        // Explicit offset stored with value — good for audit columns
        private OffsetDateTime lastModifiedAt;

        // LocalDateTime would be WRONG here — no timezone, ambiguous on read
        // private LocalDateTime createdAt; // DO NOT DO THIS

        Order(String orderId, LocalDate deliveryDate,
              Instant createdAt, OffsetDateTime lastModifiedAt) {
            this.orderId = orderId;
            this.deliveryDate = deliveryDate;
            this.createdAt = createdAt;
            this.lastModifiedAt = lastModifiedAt;
        }

        // Display createdAt in a user's local timezone (UI concern only)
        String createdAtInZone(ZoneId zone) {
            return createdAt.atZone(zone).toString();
        }
    }

    public static void main(String[] args) {
        Instant now = Instant.parse("2024-06-18T14:30:00Z"); // UTC
        OffsetDateTime auditTime = OffsetDateTime.of(
                2024, 6, 18, 16, 30, 0, 0, ZoneOffset.ofHours(2));

        Order order = new Order(
                "ORD-001",
                LocalDate.of(2024, 6, 25),   // delivery_date → SQL DATE
                now,                          // created_at → SQL TIMESTAMP WITH TIME ZONE
                auditTime                     // last_modified_at → offset preserved
        );

        log.info("Order ID       : " + order.orderId);
        log.info("Delivery Date  : " + order.deliveryDate);   // date only
        log.info("Created At UTC : " + order.createdAt);      // always UTC
        log.info("Last Modified  : " + order.lastModifiedAt); // offset preserved
        log.info("Created (Tokyo): " + order.createdAtInZone(ZoneId.of("Asia/Tokyo")));
        log.info("Created (NY)   : " + order.createdAtInZone(ZoneId.of("America/New_York")));

        // Output:
        // Order ID       : ORD-001
        // Delivery Date  : 2024-06-25
        // Created At UTC : 2024-06-18T14:30:00Z
        // Last Modified  : 2024-06-18T16:30:00+02:00
        // Created (Tokyo): 2024-06-18T23:30:00+09:00[Asia/Tokyo]
        // Created (NY)   : 2024-06-18T10:30:00-04:00[America/New_York]
    }
}