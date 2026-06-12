// Java 8+
/**
 * Listing 9.6 — TimeZoneModelling.java
 * Demonstrates: ZoneId, ZonedDateTime, cross-zone conversion, and Instant round-trip
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

        // ZonedDateTime — a specific moment anchored to a full timezone rule set
        ZonedDateTime meetingInMumbai = ZonedDateTime.of(
                2024, 6, 18, 14, 0, 0, 0, kolkata);      // 14:00 IST

        // Convert to other zones — same instant, different local display
        ZonedDateTime meetingInLondon  = meetingInMumbai.withZoneSameInstant(london);
        ZonedDateTime meetingInNewYork = meetingInMumbai.withZoneSameInstant(newYork);
        ZonedDateTime meetingInUtc     = meetingInMumbai.withZoneSameInstant(utc);

        LOG.info("Mumbai  : " + meetingInMumbai);   // 14:00+05:30
        LOG.info("London  : " + meetingInLondon);   // 09:30+01:00[BST]
        LOG.info("New York: " + meetingInNewYork);  // 04:30-04:00[EDT]
        LOG.info("UTC     : " + meetingInUtc);      // 08:30Z

        // Store as UTC Instant — timezone context stripped, pure point in time
        Instant asInstant = meetingInMumbai.toInstant();
        LOG.info("As Instant (UTC): " + asInstant); // 2024-06-18T08:30:00Z

        // Reconstruct for any viewer's timezone from the stored Instant
        ZonedDateTime forViewer = asInstant.atZone(kolkata);
        LOG.info("Reconstructed for Kolkata viewer: " + forViewer);

        // Demonstrate that all three ZonedDateTimes represent the same instant
        boolean sameInstant = meetingInMumbai.toInstant()
                .equals(meetingInLondon.toInstant());
        LOG.info("Mumbai and London represent same instant: " + sameInstant); // true

        // Output:
        // Mumbai  : 2024-06-18T14:00+05:30[Asia/Kolkata]
        // London  : 2024-06-18T09:30+01:00[Europe/London]
        // New York: 2024-06-18T04:30-04:00[America/New_York]
        // UTC     : 2024-06-18T08:30Z
        // As Instant (UTC): 2024-06-18T08:30:00Z
        // Reconstructed for Kolkata viewer: 2024-06-18T14:00+05:30[Asia/Kolkata]
        // Mumbai and London represent same instant: true
    }
}