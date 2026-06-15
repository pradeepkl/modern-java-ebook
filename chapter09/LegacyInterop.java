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
        Instant fromDate = legacyDate.toInstant();
        LOG.info("java.util.Date -> Instant: " + fromDate);

        // Instant -> Date
        Instant instant = Instant.now();
        java.util.Date toDate = java.util.Date.from(instant);
        LOG.info("Instant -> java.util.Date: " + toDate);

        // Calendar -> Instant
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        Instant fromCalendar = calendar.toInstant();
        LOG.info("java.util.Calendar -> Instant: " + fromCalendar);

        // LocalDate -> java.sql.Date (for JDBC)
        LocalDate localDate = LocalDate.of(2024, 6, 18);
        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
        LOG.info("LocalDate -> java.sql.Date: " + sqlDate);

        // java.sql.Date -> LocalDate
        LocalDate fromSql = sqlDate.toLocalDate();
        LOG.info("java.sql.Date -> LocalDate: " + fromSql);

        // The conversion pattern:
        // Legacy -> Instant -> modern java.time type
        // Modern java.time type -> Instant -> Legacy
        // Instant is the bridge between worlds
        LOG.info("Conversion pattern: Legacy <-> Instant <-> java.time");

        // Output:
        // java.util.Date -> Instant: 2024-06-18T10:15:30.123Z
        // Instant -> java.util.Date: Tue Jun 18 10:15:30 UTC 2024
        // java.util.Calendar -> Instant: 2024-06-18T10:15:30.456Z
        // LocalDate -> java.sql.Date: 2024-06-18
        // java.sql.Date -> LocalDate: 2024-06-18
        // Conversion pattern: Legacy <-> Instant <-> java.time
    }
}