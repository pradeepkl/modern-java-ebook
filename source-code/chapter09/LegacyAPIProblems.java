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
        Date orderDate = new Date(1_700_000_000_000L); // some order date
        Date reference = orderDate;                     // both point to same object
        LOG.info("Before mutation: orderDate = " + orderDate);

        reference.setTime(0); // modifies orderDate too — silent corruption
        LOG.info("After mutation via reference: orderDate = " + orderDate);
        // orderDate is now 1970-01-01 — corrupted silently

        // Problem 2: Calendar months are zero-indexed — January is 0
        Calendar cal = Calendar.getInstance();
        cal.set(2024, 0, 15); // 0 = January — not December (11)
        LOG.info("Calendar month 0 = January: " + cal.getTime());

        cal.set(2024, 11, 15); // 11 = December — counterintuitive
        LOG.info("Calendar month 11 = December: " + cal.getTime());

        // Problem 3: SimpleDateFormat is NOT thread-safe
        // Sharing across threads causes unpredictable output or exceptions
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        // NEVER share this instance across threads
        // Each thread must create its own instance
        try {
            String formatted = formatter.format(new Date(1_700_000_000_000L));
            LOG.info("Formatted (single-threaded, safe here): " + formatted);
        } catch (Exception e) {
            LOG.warning("Formatting failed: " + e.getMessage());
        }

        LOG.info("Use java.time.LocalDate, ZonedDateTime, DateTimeFormatter instead.");
        // DateTimeFormatter is immutable and thread-safe — always prefer it
    }

    // Output:
    // Before mutation: orderDate = <some date>
    // After mutation via reference: orderDate = Thu Jan 01 05:30:00 IST 1970
    // Calendar month 0 = January: Mon Jan 15 ...
    // Calendar month 11 = December: Sun Dec 15 ...
    // Formatted (single-threaded, safe here): 2023-11-14
    // Use java.time.LocalDate, ZonedDateTime, DateTimeFormatter instead.
}