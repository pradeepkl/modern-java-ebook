// Java 25+
// Feature shown: LocalDateTime and ZonedDateTime, final in Java 8+
package chapter09;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.Duration;
import java.util.logging.Logger;

/**
 * Listing 9.1 — TimeIsContextual.java
 * Demonstrates: LocalDateTime vs ZonedDateTime — why time requires context
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+ for java.time types; Java 25+ for void main() instance
 * main method (JEP 512: Compact Source Files and Instance Main Methods)
 */
public class TimeIsContextual {

    private static final Logger LOG =
            Logger.getLogger(TimeIsContextual.class.getName());

    void main() {
        // NOT IDEAL: What does this mean?
        // Is this Mumbai time? London time? UTC?
        LocalDateTime ambiguous =
                LocalDateTime.of(2024, 3, 15, 9, 0);

        LOG.info("Ambiguous local date-time (no zone): " + ambiguous);

        // Correct approach: This is unambiguous — a specific moment
        // in a specific time zone
        ZonedDateTime mumbaiMorning =
                ZonedDateTime.of(2024, 3, 15, 9, 0, 0, 0,
                        ZoneId.of("Asia/Kolkata"));

        ZonedDateTime londonMorning =
                ZonedDateTime.of(2024, 3, 15, 9, 0, 0, 0,
                        ZoneId.of("Europe/London"));

        LOG.info("Mumbai 9:00 as Instant:  " + mumbaiMorning.toInstant());
        LOG.info("London 9:00 as Instant:  " + londonMorning.toInstant());

        // These are different moments in time
        // Mumbai 9:00 is 3.5 hours ahead of London
        boolean sameInstant = mumbaiMorning.toInstant()
                .equals(londonMorning.toInstant());

        LOG.info("Same instant? " + sameInstant); // false

        // Quantify the difference: Mumbai IST is UTC+5:30, London GMT is UTC+0
        Duration gap = Duration.between(mumbaiMorning.toInstant(),
                londonMorning.toInstant());

        LOG.info("London is " + Math.abs(gap.toMinutes())
                + " minutes after Mumbai 9:00");

        // Output:
        // Ambiguous local date-time (no zone): 2024-03-15T09:00
        // Mumbai 9:00 as Instant:  2024-03-15T03:30:00Z
        // London 9:00 as Instant:  2024-03-15T09:00:00Z
        // Same instant? false
        // London is 330 minutes after Mumbai 9:00
    }
}