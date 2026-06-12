// Java 8+
/**
 * Listing 9.5 — InstantVsOffsetVsZoned.java
 * Demonstrates: Differences between Instant, OffsetDateTime, and ZonedDateTime
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+
 */
package chapter09;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

public class InstantVsOffsetVsZoned {

    private static final Logger LOG = Logger.getLogger(InstantVsOffsetVsZoned.class.getName());

    public static void main(String[] args) {

        // Instant — UTC only, no offset, no timezone
        Instant instant = Instant.now();
        LOG.info("Instant (UTC only): " + instant);

        // OffsetDateTime — timestamp + fixed offset (+05:30 = IST, never changes with DST)
        OffsetDateTime withOffset = OffsetDateTime.now(ZoneOffset.of("+05:30"));
        LOG.info("OffsetDateTime (IST fixed +05:30): " + withOffset);

        // ZonedDateTime — timestamp + full timezone rules (DST-aware)
        ZonedDateTime withZone = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LOG.info("ZonedDateTime (Europe/London, DST-aware): " + withZone);

        // Converting OffsetDateTime and ZonedDateTime back to Instant (UTC, context lost)
        Instant fromOffset = withOffset.toInstant();
        Instant fromZoned  = withZone.toInstant();
        LOG.info("Instant from OffsetDateTime: " + fromOffset);
        LOG.info("Instant from ZonedDateTime:  " + fromZoned);

        // Instant → OffsetDateTime with explicit UTC offset
        OffsetDateTime fromInstant = instant.atOffset(ZoneOffset.UTC);
        LOG.info("Instant -> OffsetDateTime (UTC): " + fromInstant);

        // Instant → ZonedDateTime in IST (Asia/Kolkata, DST-aware)
        ZonedDateTime fromInstantZoned = instant.atZone(ZoneId.of("Asia/Kolkata"));
        LOG.info("Instant -> ZonedDateTime (Asia/Kolkata): " + fromInstantZoned);

        // Key difference: OffsetDateTime preserves offset at creation time
        // ZonedDateTime applies current DST rules for the zone
        ZonedDateTime londonJune = ZonedDateTime.of(
                2024, 6, 18, 9, 30, 0, 0, ZoneId.of("Europe/London"));
        ZonedDateTime londonDecember = ZonedDateTime.of(
                2024, 12, 18, 9, 30, 0, 0, ZoneId.of("Europe/London"));
        LOG.info("London June (BST, DST on):  " + londonJune);       // +01:00
        LOG.info("London December (GMT, DST off): " + londonDecember); // +00:00

        // Output:
        // Instant (UTC only): 2024-06-18T08:30:00.123456Z
        // OffsetDateTime (IST fixed +05:30): 2024-06-18T14:00:00.123456+05:30
        // ZonedDateTime (Europe/London, DST-aware): 2024-06-18T09:30:00.123456+01:00[Europe/London]
        // Instant from OffsetDateTime: 2024-06-18T08:30:00.123456Z
        // Instant from ZonedDateTime:  2024-06-18T08:30:00.123456Z
        // Instant -> OffsetDateTime (UTC): 2024-06-18T08:30:00.123456Z
        // Instant -> ZonedDateTime (Asia/Kolkata): 2024-06-18T14:00:00.123456+05:30[Asia/Kolkata]
        // London June (BST, DST on):  2024-06-18T09:30+01:00[Europe/London]
        // London December (GMT, DST off): 2024-12-18T09:30Z[Europe/London]
    }
}