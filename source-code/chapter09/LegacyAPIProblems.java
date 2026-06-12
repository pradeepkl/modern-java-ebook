// Java 8+
/**
 * Listing 9.2 — LegacyAPIProblems.java
 * Demonstrates: Problems with java.util.Date, Calendar, and SimpleDateFormat
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+
 */
package chapter09;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class LegacyAPIProblems {

    private static final Logger LOG = Logger.getLogger(LegacyAPIProblems.class.getName());

    public static void main(String[] args) {

        // Problem 1: Date is mutable — aliasing causes silent corruption
        Date orderDate = new Date();
        Date reference = orderDate; // both point to same object
        LOG.info("Before mutation: " + orderDate);

        reference.setTime(0); // modifies orderDate too — silent corruption
        LOG.info("After reference.setTime(0): " + orderDate);
        // orderDate is now 1970-01-01 — corrupted silently
        LOG.info("Same object? " + (orderDate == reference)); // true

        // Problem 2: Calendar months are zero-indexed — January is 0
        Calendar cal = Calendar.getInstance();
        cal.set(2024, 0, 15); // 0 = January — not December (11)
        LOG.info("Calendar month 0 = January: " + cal.getTime());

        cal.set(2024, 11, 15); // 11 = December — confusing but correct
        LOG.info("Calendar month 11 = December: " + cal.getTime());

        // Problem 3: SimpleDateFormat is NOT thread-safe
        // Sharing across threads causes unpredictable output or exceptions
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        // NEVER share this instance across threads
        // Each thread must create its own instance
        // Prefer java.time.format.DateTimeFormatter — immutable and thread-safe
        try {
            String formatted = formatter.format(new Date(0)); // epoch
            LOG.info("Formatted epoch (single thread, safe here): " + formatted);
        } catch (Exception e) {
            LOG.warning("Formatting failed: " + e.getMessage());
        }

        LOG.info("Use java.time.LocalDate, ZonedDateTime, DateTimeFormatter instead.");

        // Output:
        // Before mutation: <current date>
        // After reference.setTime(0): Thu Jan 01 05:30:00 IST 1970
        // Same object? true
        // Calendar month 0 = January: Mon Jan 15 ...
        // Calendar month 11 = December: Sun Dec 15 ...
        // Formatted epoch (single thread, safe here): 1970-01-01
        // Use java.time.LocalDate, ZonedDateTime, DateTimeFormatter instead.
    }
}