// Java 25+
// Feature shown: legacy date/time API problems (Date, Calendar, SimpleDateFormat), final in Java 1.0+

/**
 * Listing 9.2 — LegacyAPIProblems.java
 * Demonstrates: Mutability of Date, zero-indexed months in Calendar,
 *               and thread-unsafety of SimpleDateFormat
 * Chapter 9: Modern Date and Time
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter09;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class LegacyAPIProblems {

    private static final Logger LOG =
            Logger.getLogger(LegacyAPIProblems.class.getName());

    void main() {

        // Problem 1: Date is mutable
        // Any code holding a reference can change
        // the date out from under you
        Date orderDate = new Date();
        Date reference = orderDate;
        reference.setTime(0); // modifies orderDate too
        // orderDate is now 1970-01-01 — corrupted silently
        LOG.info("orderDate after mutation via reference: " + orderDate);
        // Both variables point to the same object — mutation is invisible

        // Problem 2: Calendar API is confusing
        // Months are zero-indexed — January is 0
        Calendar cal = Calendar.getInstance();
        cal.set(2024, 0, 15); // January 15 — not December 15
        // Every developer has been burned by this once
        int month = cal.get(Calendar.MONTH); // returns 0, not 1
        LOG.info("Calendar month for January: " + month
                + " (zero-indexed — 0 means January)");

        // Problem 3: SimpleDateFormat is NOT thread-safe
        // Sharing a formatter across threads causes
        // unpredictable output or exceptions
        SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd");
        // NEVER share this across threads
        // Each thread needs its own instance
        // Or use DateTimeFormatter (thread-safe)
        try {
            String formatted = formatter.format(new Date(0));
            LOG.info("Formatted epoch: " + formatted);
        } catch (Exception e) {
            LOG.warning("Formatting failed: " + e.getMessage());
        }

        LOG.info("Use java.time classes instead:"
                + " LocalDate, ZonedDateTime, DateTimeFormatter");

        // Output:
        // orderDate after mutation via reference: Thu Jan 01 05:30:00 IST 1970
        // Calendar month for January: 0 (zero-indexed — 0 means January)
        // Formatted epoch: 1970-01-01
        // Use java.time classes instead: LocalDate, ZonedDateTime, DateTimeFormatter
    }
}