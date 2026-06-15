// Java 25+
// Feature shown: common date-time anti-patterns and correct alternatives, final in Java 8+

/**
 * Listing 9.12 — TimeBugs.java
 * Demonstrates: common date-time anti-patterns and their correct alternatives
 * Chapter 9: Modern Date and Time
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter09;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class TimeBugs {

    private static final Logger LOG = Logger.getLogger(TimeBugs.class.getName());

    // NOT IDEAL: Shared SimpleDateFormat — not thread-safe
    static SimpleDateFormat sharedFormatter =
            new SimpleDateFormat("yyyy-MM-dd");
    // Multiple threads calling sharedFormatter.format()
    // simultaneously will corrupt each other's output

    // Correct approach: DateTimeFormatter — immutable, always safe to share
    static final DateTimeFormatter SAFE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    void main() {
        // Bug 1: LocalDateTime for audit timestamps — no timezone, ambiguous
        LocalDateTime auditTime = LocalDateTime.now();
        LOG.info("Ambiguous audit time (no zone): " + auditTime);

        // Correct: Instant for audit timestamps — always UTC, unambiguous
        Instant auditInstant = Instant.now();
        LOG.info("Unambiguous audit instant (UTC): " + auditInstant);

        // Bug 2: Storing user appointment as Instant — loses DST meaning
        Instant appointmentWrong = Instant.now();
        LOG.info("Appointment as Instant (loses DST context): " + appointmentWrong);

        // Correct: ZonedDateTime preserves timezone rules including DST
        ZonedDateTime appointmentRight =
                ZonedDateTime.of(2024, 6, 18, 14, 0, 0, 0,
                        ZoneId.of("Asia/Kolkata"));
        LOG.info("Appointment as ZonedDateTime (DST-aware): " + appointmentRight);

        // Bug 3: Hardcoded UTC offset — wrong for London half the year
        ZoneOffset hardcoded = ZoneOffset.of("+01:00");
        LOG.info("Hardcoded offset (ignores DST rules): " + hardcoded);

        // Correct: ZoneId knows DST rules for the named region
        ZoneId london = ZoneId.of("Europe/London");
        LOG.info("Named zone (DST-aware): " + london);

        // Output:
        // Ambiguous audit time (no zone): 2024-06-18T14:00:00.123456
        // Unambiguous audit instant (UTC): 2024-06-18T08:30:00.123456Z
        // Appointment as Instant (loses DST context): 2024-06-18T08:30:00.123456Z
        // Appointment as ZonedDateTime (DST-aware): 2024-06-18T14:00+05:30[Asia/Kolkata]
        // Hardcoded offset (ignores DST rules): +01:00
        // Named zone (DST-aware): Europe/London
    }
}