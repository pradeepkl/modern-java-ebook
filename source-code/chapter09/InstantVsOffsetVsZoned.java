// Java 25+
// Feature shown: Instant, OffsetDateTime, ZonedDateTime comparison, final in Java 8+

/**
 * Listing 9.5 — InstantVsOffsetVsZoned.java
 * Demonstrates: Instant, OffsetDateTime, and ZonedDateTime differences
 * Chapter 9: Modern Date and Time
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter09;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.logging.Logger;

public class InstantVsOffsetVsZoned {

    private static final Logger LOG =
            Logger.getLogger(InstantVsOffsetVsZoned.class.getName());

    void main() {
        // Instant — UTC only, no offset, no timezone
        Instant instant = Instant.now();
        LOG.info("Instant (UTC): " + instant);

        // OffsetDateTime — timestamp + fixed offset (IST, never changes with DST)
        OffsetDateTime withOffset = OffsetDateTime.now(
                ZoneOffset.of("+05:30")); // IST fixed offset
        LOG.info("OffsetDateTime IST: " + withOffset);

        // OffsetDateTime with London summer offset (+01:00, BST)
        // Offset is preserved at creation time — does not adjust for DST later
        OffsetDateTime londonSummer = OffsetDateTime.now(
                ZoneOffset.of("+01:00")); // London BST offset
        LOG.info("OffsetDateTime London summer: " + londonSummer);

        // ZonedDateTime — timestamp + full timezone rules (DST-aware)
        ZonedDateTime withZone = ZonedDateTime.now(
                ZoneId.of("Europe/London"));
        LOG.info("ZonedDateTime Europe/London: " + withZone);

        // Converting ZonedDateTime and OffsetDateTime back to Instant
        Instant fromOffset = withOffset.toInstant(); // timezone context lost
        Instant fromZoned  = withZone.toInstant();   // timezone context lost
        LOG.info("Instant from OffsetDateTime: " + fromOffset);
        LOG.info("Instant from ZonedDateTime:  " + fromZoned);

        // Instant to OffsetDateTime with UTC offset
        OffsetDateTime fromInstant = instant.atOffset(ZoneOffset.UTC);
        LOG.info("Instant -> OffsetDateTime UTC: " + fromInstant);

        // Instant to ZonedDateTime in IST
        ZonedDateTime fromInstantZoned = instant.atZone(
                ZoneId.of("Asia/Kolkata"));
        LOG.info("Instant -> ZonedDateTime IST: " + fromInstantZoned);

        // Output:
        // Instant (UTC): 2024-06-18T08:30:00.123456Z
        // OffsetDateTime IST: 2024-06-18T14:00:00.123456+05:30
        // OffsetDateTime London summer: 2024-06-18T09:30:00.123456+01:00
        // ZonedDateTime Europe/London: 2024-06-18T09:30:00.123456+01:00[Europe/London]
        // Instant from OffsetDateTime: 2024-06-18T08:30:00.123456Z
        // Instant from ZonedDateTime:  2024-06-18T08:30:00.123456Z
        // Instant -> OffsetDateTime UTC: 2024-06-18T08:30:00.123456Z
        // Instant -> ZonedDateTime IST: 2024-06-18T14:00:00.123456+05:30[Asia/Kolkata]
    }
}