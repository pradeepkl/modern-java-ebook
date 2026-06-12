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
        // e.g. 2024-06-18T08:30:00Z — Z means UTC

        // OffsetDateTime — timestamp + fixed offset (IST = +05:30, no DST)
        OffsetDateTime withOffset = OffsetDateTime.now(ZoneOffset.of("+05:30"));
        LOG.info("OffsetDateTime (+05:30): " + withOffset);
        // e.g. 2024-06-18T14:00:00+05:30 — offset is fixed, never changes

        // ZonedDateTime — timestamp + full timezone rules (DST-aware)
        ZonedDateTime withZone = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LOG.info("ZonedDateTime (Europe/London): " + withZone);
        // e.g. 2024-06-18T09:30:00+01:00[Europe/London] — BST in June

        // Converting OffsetDateTime and ZonedDateTime back to Instant
        Instant fromOffset = withOffset.toInstant(); // timezone context lost
        Instant fromZoned  = withZone.toInstant();   // timezone context lost
        LOG.info("Instant from OffsetDateTime: " + fromOffset);
        LOG.info("Instant from ZonedDateTime:  " + fromZoned);

        // Converting Instant to OffsetDateTime with UTC offset
        OffsetDateTime fromInstant = instant.atOffset(ZoneOffset.UTC);
        LOG.info("Instant -> OffsetDateTime (UTC): " + fromInstant);

        // Converting Instant to ZonedDateTime in IST
        ZonedDateTime fromInstantZoned = instant.atZone(ZoneId.of("Asia/Kolkata"));
        LOG.info("Instant -> ZonedDateTime (Asia/Kolkata): " + fromInstantZoned);

        // Key difference: fixed offset vs DST-aware zone
        ZonedDateTime londonJune = ZonedDateTime.of(2024, 6, 18, 9, 30, 0, 0,
                ZoneId.of("Europe/London"));
        ZonedDateTime londonDec  = ZonedDateTime.of(2024, 12, 18, 9, 30, 0, 0,
                ZoneId.of("Europe/London"));
        LOG.info("London June offset:    " + londonJune.getOffset()); // +01:00 BST
        LOG.info("London December offset:" + londonDec.getOffset());  // +00:00 GMT

        // Output:
        // Instant (UTC only): 2024-06-18T08:30:00.123Z
        // OffsetDateTime (+05:30): 2024-06-18T14:00:00.123+05:30
        // ZonedDateTime (Europe/London): 2024-06-18T09:30:00.123+01:00[Europe/London]
        // Instant from OffsetDateTime: 2024-06-18T08:30:00.123Z
        // Instant from ZonedDateTime:  2024-06-18T08:30:00.123Z
        // Instant -> OffsetDateTime (UTC): 2024-06-18T08:30:00.123Z
        // Instant -> ZonedDateTime (Asia/Kolkata): 2024-06-18T14:00:00.123+05:30[Asia/Kolkata]
        // London June offset:    +01:00
        // London December offset:+00:00
    }
}