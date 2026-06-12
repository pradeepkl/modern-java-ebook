// Java 8+
/**
 * Listing 9.1 — TimeIsContextual.java
 * Demonstrates: Why LocalDateTime is ambiguous and ZonedDateTime is not
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

    private static final Logger logger = Logger.getLogger(TimeIsContextual.class.getName());

    public static void main(String[] args) {

        // NOT IDEAL: no time zone — what clock face does this refer to?
        LocalDateTime ambiguous = LocalDateTime.of(2024, 3, 15, 9, 0);
        logger.info("Ambiguous LocalDateTime: " + ambiguous
                + " (no zone — Mumbai? London? UTC?)");

        // Correct: ZonedDateTime carries full context
        ZonedDateTime mumbaiMorning =
                ZonedDateTime.of(2024, 3, 15, 9, 0, 0, 0,
                        ZoneId.of("Asia/Kolkata"));   // UTC+5:30

        ZonedDateTime londonMorning =
                ZonedDateTime.of(2024, 3, 15, 9, 0, 0, 0,
                        ZoneId.of("Europe/London"));  // UTC+0:00

        logger.info("Mumbai  9:00 as Instant: " + mumbaiMorning.toInstant());
        logger.info("London  9:00 as Instant: " + londonMorning.toInstant());

        // Compare the underlying machine moments
        boolean sameInstant = mumbaiMorning.toInstant()
                .equals(londonMorning.toInstant());
        logger.info("Same instant in time? " + sameInstant); // false

        // Quantify the difference: Mumbai is 5h30m ahead of London
        Duration difference = Duration.between(
                mumbaiMorning.toInstant(),
                londonMorning.toInstant());
        logger.info("London 9:00 is " + Math.abs(difference.toMinutes())
                + " minutes after Mumbai 9:00");

        // Output:
        // Ambiguous LocalDateTime: 2024-03-15T09:00 (no zone — Mumbai? London? UTC?)
        // Mumbai  9:00 as Instant: 2024-03-15T03:30:00Z
        // London  9:00 as Instant: 2024-03-15T09:00:00Z
        // Same instant in time? false
        // London 9:00 is 330 minutes after Mumbai 9:00
    }
}