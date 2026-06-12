// Java 8+
/**
 * Listing 9.1 — TimeIsContextual.java
 * Demonstrates: Why time requires context — the same clock value
 *               represents different moments in different time zones
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+
 */
package chapter09;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.Duration;
import java.util.logging.Logger;

public class TimeIsContextual {

    private static final Logger LOG = Logger.getLogger(TimeIsContextual.class.getName());

    public static void main(String[] args) {

        // Ambiguous: no time zone — which 9:00 is this?
        LocalDateTime ambiguous = LocalDateTime.of(2024, 3, 15, 9, 0);
        LOG.info("Ambiguous (no zone): " + ambiguous);

        // Unambiguous: 9:00 AM in Mumbai (IST = UTC+5:30)
        ZonedDateTime mumbaiMorning = ZonedDateTime.of(
                2024, 3, 15, 9, 0, 0, 0,
                ZoneId.of("Asia/Kolkata"));

        // Unambiguous: 9:00 AM in London (GMT = UTC+0)
        ZonedDateTime londonMorning = ZonedDateTime.of(
                2024, 3, 15, 9, 0, 0, 0,
                ZoneId.of("Europe/London"));

        LOG.info("Mumbai  9:00 as Instant: " + mumbaiMorning.toInstant());
        LOG.info("London  9:00 as Instant: " + londonMorning.toInstant());

        // Same clock face, different moments in time
        boolean sameInstant = mumbaiMorning.toInstant()
                .equals(londonMorning.toInstant());
        LOG.info("Same instant? " + sameInstant); // false

        // Mumbai is 5h30m ahead of London — quantify the gap
        Duration gap = Duration.between(
                mumbaiMorning.toInstant(),
                londonMorning.toInstant());
        LOG.info("London 9:00 is " + gap.toMinutes()
                + " minutes after Mumbai 9:00"); // 330 minutes = 5h30m

        // Output:
        // Ambiguous (no zone): 2024-03-15T09:00
        // Mumbai  9:00 as Instant: 2024-03-15T03:30:00Z
        // London  9:00 as Instant: 2024-03-15T09:00:00Z
        // Same instant? false
        // London 9:00 is 330 minutes after Mumbai 9:00
    }
}