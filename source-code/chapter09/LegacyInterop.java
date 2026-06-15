// Java 25+
// Feature shown: legacy Date/Calendar to java.time conversion, final in Java 8+

/**
 * Listing 9.13 — LegacyInterop.java
 * Demonstrates: Converting between legacy java.util.Date, java.util.Calendar,
 * java.sql.Date and modern java.time types using Instant as the bridge.
 * Chapter 9: Modern Date and Time
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter09;

import java.time.Instant;
import java.time.LocalDate;
import java.util.logging.Logger;

public class LegacyInterop {

    private static final Logger LOG = Logger.getLogger(LegacyInterop.class.getName());

    void main() {

        // Date -> Instant (the conversion hub)
        java.util.Date legacyDate = new java.util.Date();
        Instant fromDate = legacyDate.toInstant(); // Date carries an Instant internally
        LOG.info("Legacy Date -> Instant: " + fromDate);

        // Instant -> Date
        Instant instant = Instant.now();
        java.util.Date toDate = java.util.Date.from(instant); // reverse bridge
        LOG.info("Instant -> Legacy Date: " + toDate);

        // Calendar -> Instant
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        Instant fromCalendar = calendar.toInstant(); // Calendar also exposes toInstant()
        LOG.info("Legacy Calendar -> Instant: " + fromCalendar);

        // LocalDate -> java.sql.Date (for JDBC)
        LocalDate localDate = LocalDate.of(2024, 6, 18);
        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate); // direct valueOf bridge
        LOG.info("LocalDate -> java.sql.Date: " + sqlDate);

        // java.sql.Date -> LocalDate
        LocalDate fromSql = sqlDate.toLocalDate(); // reverse bridge
        LOG.info("java.sql.Date -> LocalDate: " + fromSql);

        // The conversion pattern:
        // Legacy -> Instant -> modern java.time type
        // Modern java.time type -> Instant -> Legacy
        // Instant is the bridge between worlds
        LOG.info("Round-trip check, dates match: " + localDate.equals(fromSql));

        // Output:
        // Legacy Date -> Instant: 2024-06-18T10:15:30.123Z
        // Instant -> Legacy Date: Tue Jun 18 10:15:30 UTC 2024
        // Legacy Calendar -> Instant: 2024-06-18T10:15:30.456Z
        // LocalDate -> java.sql.Date: 2024-06-18
        // java.sql.Date -> LocalDate: 2024-06-18
        // Round-trip check, dates match: true
    }
}