// Java 8+
/**
 * Listing 9.13 — LegacyInterop.java
 * Demonstrates: Converting between legacy java.util.Date/Calendar and modern java.time types
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+
 */
package chapter09;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class LegacyInterop {

    private static final Logger LOG = Logger.getLogger(LegacyInterop.class.getName());

    public static void main(String[] args) {

        // Date → Instant (the conversion hub)
        Date legacyDate = new Date();
        Instant fromDate = legacyDate.toInstant(); // Date exposes toInstant() since Java 8
        LOG.info("Date → Instant: " + fromDate);

        // Instant → Date
        Instant instant = Instant.now();
        Date toDate = Date.from(instant); // Date.from() is the reverse bridge
        LOG.info("Instant → Date: " + toDate);

        // Calendar → Instant
        Calendar calendar = Calendar.getInstance();
        Instant fromCalendar = calendar.toInstant(); // Calendar also exposes toInstant()
        LOG.info("Calendar → Instant: " + fromCalendar);

        // LocalDate → java.sql.Date (for JDBC)
        LocalDate localDate = LocalDate.of(2024, 6, 18);
        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate); // Direct valueOf conversion
        LOG.info("LocalDate → sql.Date: " + sqlDate);

        // java.sql.Date → LocalDate
        LocalDate fromSql = sqlDate.toLocalDate(); // sql.Date exposes toLocalDate()
        LOG.info("sql.Date → LocalDate: " + fromSql);

        // The conversion pattern:
        // Legacy → Instant → modern java.time type
        // Modern java.time type → Instant → Legacy
        // Instant is the bridge between worlds
        LOG.info("Conversion round-trip verified: " + fromSql.equals(localDate));
    }
    // Output:
    // Date → Instant: 2024-06-18T10:15:30.123Z
    // Instant → Date: Tue Jun 18 10:15:30 UTC 2024
    // Calendar → Instant: 2024-06-18T10:15:30.456Z
    // LocalDate → sql.Date: 2024-06-18
    // sql.Date → LocalDate: 2024-06-18
    // Conversion round-trip verified: true
}