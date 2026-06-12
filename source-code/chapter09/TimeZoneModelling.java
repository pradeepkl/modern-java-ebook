// Java 8+
/**
 * Listing 9.6 — TimeZoneModelling.java
 * Demonstrates: ZoneId, ZonedDateTime, and cross-timezone conversion
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+
 */
package chapter09;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

public class TimeZoneModelling {

    private static final Logger LOG = Logger.getLogger(TimeZoneModelling.class.getName());

    public static void main(String[] args) {

        // ZoneId — identifies a time zone by its IANA name
        ZoneId kolkata = ZoneId.of("Asia/Kolkata");       // UTC+5:30, no DST
        ZoneId london  = ZoneId.of("Europe/London");      // UTC+0 / BST UTC+1
        ZoneId newYork = ZoneId.of("America/New_York");   // EST UTC-5 / EDT UTC-4
        ZoneId utc     = ZoneId.of("UTC");                // always UTC+0

        // ZonedDateTime — a specific moment anchored to a specific zone
        ZonedDateTime meetingInMumbai = ZonedDateTime.of(
                2024, 6, 18, 14, 0, 0, 0, kolkata);      // 14:00 IST

        // Convert to other zones — same instant, different local display
        ZonedDateTime meetingInLondon  = meetingInMumbai
                .withZoneSameInstant(london);             // 09:30 BST (UTC+1)
        ZonedDateTime meetingInNewYork = meetingInMumbai
                .withZoneSameInstant(newYork);            // 04:30 EDT (UTC-4)
        ZonedDateTime meetingInUtc     = meetingInMumbai
                .withZoneSameInstant(utc);                // 08:30 UTC

        LOG.info("Mumbai  : " + meetingInMumbai);
        LOG.info("London  : " + meetingInLondon);
        LOG.info("New York: " + meetingInNewYork);
        LOG.info("UTC     : " + meetingInUtc);

        // Store as UTC Instant — timezone-neutral representation
        Instant asInstant = meetingInMumbai.toInstant();
        LOG.info("As Instant (UTC epoch): " + asInstant);

        // Reconstruct for any viewer's timezone from the same Instant
        ZonedDateTime forViewerKolkata = asInstant.atZone(kolkata);
        ZonedDateTime forViewerLondon  = asInstant.atZone(london);
        LOG.info("Reconstructed Kolkata : " + forViewerKolkata);
        LOG.info("Reconstructed London  : " + forViewerLondon);

        // Output:
        // Mumbai  : 2024-06-18T14:00+05:30[Asia/Kolkata]
        // London  : 2024-06-18T09:30+01:00[Europe/London]
        // New York: 2024-06-18T04:30-04:00[America/New_York]
        // UTC     : 2024-06-18T08:30Z[UTC]
        // As Instant (UTC epoch): 2024-06-18T08:30:00Z
        // Reconstructed Kolkata : 2024-06-18T14:00+05:30[Asia/Kolkata]
        // Reconstructed London  : 2024-06-18T09:30+01:00[Europe/London]
    }
}