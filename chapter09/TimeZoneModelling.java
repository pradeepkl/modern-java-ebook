// Java 25+
// Feature shown: ZoneId, ZonedDateTime, withZoneSameInstant, final in Java 8+

/**
 * Listing 9.6 — TimeZoneModelling.java
 * Demonstrates: ZoneId, ZonedDateTime, and withZoneSameInstant for
 * converting a meeting time across multiple time zones
 * Chapter 9: Modern Date and Time
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter09;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class TimeZoneModelling {

    private static final Logger LOG = Logger.getLogger(TimeZoneModelling.class.getName());

    void main() {

        // ZoneId — identifies a time zone by its IANA name
        ZoneId kolkata = ZoneId.of("Asia/Kolkata");
        ZoneId london  = ZoneId.of("Europe/London");
        ZoneId newYork = ZoneId.of("America/New_York");
        ZoneId utc     = ZoneId.of("UTC");

        // ZonedDateTime — a specific moment in a specific zone
        ZonedDateTime meetingInMumbai = ZonedDateTime.of(
                2024, 6, 18, 14, 0, 0, 0, kolkata);

        // Convert to other zones — same instant, different display
        ZonedDateTime meetingInLondon  = meetingInMumbai
                .withZoneSameInstant(london);   // IST is UTC+5:30, BST is UTC+1
        ZonedDateTime meetingInNewYork = meetingInMumbai
                .withZoneSameInstant(newYork);  // EDT is UTC-4
        ZonedDateTime meetingInUtc     = meetingInMumbai
                .withZoneSameInstant(utc);      // UTC baseline

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm z");

        LOG.info("Mumbai  : " + meetingInMumbai.format(fmt));
        LOG.info("London  : " + meetingInLondon.format(fmt));
        LOG.info("New York: " + meetingInNewYork.format(fmt));
        LOG.info("UTC     : " + meetingInUtc.format(fmt));

        // Store as UTC Instant — zone-neutral representation
        Instant asInstant = meetingInMumbai.toInstant();
        LOG.info("As Instant (UTC): " + asInstant);

        // Reconstruct for any viewer's timezone from the same Instant
        ZonedDateTime forViewer = asInstant.atZone(kolkata);
        LOG.info("Reconstructed for Kolkata viewer: " + forViewer.format(fmt));

        // Output:
        // Mumbai  : 2024-06-18 14:00 IST
        // London  : 2024-06-18 09:30 BST
        // New York: 2024-06-18 04:30 EDT
        // UTC     : 2024-06-18 08:30 UTC
        // As Instant (UTC): 2024-06-18T08:30:00Z
        // Reconstructed for Kolkata viewer: 2024-06-18 14:00 IST
    }
}