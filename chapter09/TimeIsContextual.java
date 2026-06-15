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
        LocalDateTime ambiguous =
                LocalDateTime.of(2024, 3, 15, 9, 0);

        LOG.info("Ambiguous local date-time: " + ambiguous);

        // Correct approach: unambiguous moment in a specific time zone
        ZonedDateTime mumbaiMorning =
                ZonedDateTime.of(2024, 3, 15, 9, 0, 0, 0,
                        ZoneId.of("Asia/Kolkata"));

        ZonedDateTime londonMorning =
                ZonedDateTime.of(2024, 3, 15, 9, 0, 0, 0,
                        ZoneId.of("Europe/London"));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");

        // Log each zoned moment clearly
        LOG.info("Mumbai  9:00 AM: " + mumbaiMorning.format(fmt));
        LOG.info("London  9:00 AM: " + londonMorning.format(fmt));

        // These are different moments in time
        boolean sameInstant = mumbaiMorning.toInstant()
                .equals(londonMorning.toInstant());

        // Mumbai 9:00 is 3.5 hours ahead of London 9:00
        LOG.info("Same instant in time: " + sameInstant);

        // Show the underlying Instant (epoch seconds) for each
        LOG.info("Mumbai  epoch second: " + mumbaiMorning.toInstant().getEpochSecond());
        LOG.info("London  epoch second: " + londonMorning.toInstant().getEpochSecond());

        long diffSeconds = londonMorning.toInstant().getEpochSecond()
                - mumbaiMorning.toInstant().getEpochSecond();
        LOG.info("Difference in seconds: " + diffSeconds
                + " (" + (diffSeconds / 3600.0) + " hours)");

        // Output:
        // Ambiguous local date-time: 2024-03-15T09:00
        // Mumbai  9:00 AM: 2024-03-15 09:00 IST
        // London  9:00 AM: 2024-03-15 09:00 GMT
        // Same instant in time: false
        // Mumbai  epoch second: 1710474600
        // London  epoch second: 1710491400
        // Difference in seconds: 16800 (4.666... hours)
    }
}