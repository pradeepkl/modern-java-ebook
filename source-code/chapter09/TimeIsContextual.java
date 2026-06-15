// Java 25+
// Feature shown: LocalDateTime and ZonedDateTime, final in Java 8+
package chapter09;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Listing 9.1 — TimeIsContextual.java
 * Demonstrates: LocalDateTime vs ZonedDateTime — why time requires context
 * Chapter 9: Modern Date and Time
 * Requires: Java 25+ (instance main method via JEP 512)
 */
public class TimeIsContextual {

    private static final Logger LOG = Logger.getLogger(TimeIsContextual.class.getName());

    void main() {
        // NOT IDEAL: ambiguous — no time zone, no context
        LocalDateTime ambiguous = LocalDateTime.of(2024, 3, 15, 9, 0);
        LOG.info("Ambiguous local time (no zone): " + ambiguous);

        // Correct: unambiguous moment in a specific time zone
        ZonedDateTime mumbaiMorning =
                ZonedDateTime.of(2024, 3, 15, 9, 0, 0, 0,
                        ZoneId.of("Asia/Kolkata"));

        ZonedDateTime londonMorning =
                ZonedDateTime.of(2024, 3, 15, 9, 0, 0, 0,
                        ZoneId.of("Europe/London"));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");

        // Log each zoned moment clearly
        LOG.info("Mumbai  9:00: " + mumbaiMorning.format(fmt));
        LOG.info("London  9:00: " + londonMorning.format(fmt));

        // Compare the underlying machine instants
        boolean sameInstant = mumbaiMorning.toInstant()
                .equals(londonMorning.toInstant());

        // false — Mumbai 9:00 is 3.5 hours ahead of London 9:00
        LOG.info("Same instant in time? " + sameInstant);

        // Show the offset difference in seconds
        long diffSeconds = londonMorning.toInstant().getEpochSecond()
                - mumbaiMorning.toInstant().getEpochSecond();
        long diffHours   = diffSeconds / 3600;
        long diffMinutes = (Math.abs(diffSeconds) % 3600) / 60;

        LOG.info("London is " + Math.abs(diffHours) + "h " + diffMinutes
                + "m ahead of Mumbai at the same clock reading");

        // Output:
        // Ambiguous local time (no zone): 2024-03-15T09:00
        // Mumbai  9:00: 2024-03-15 09:00 IST
        // London  9:00: 2024-03-15 09:00 GMT
        // Same instant in time? false
        // London is 3h 30m ahead of Mumbai at the same clock reading
    }
}