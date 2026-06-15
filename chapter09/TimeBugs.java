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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class TimeBugs {

    private static final Logger LOG = Logger.getLogger(TimeBugs.class.getName());

    // NOT IDEAL: SimpleDateFormat is not thread-safe — concurrent calls corrupt output
    // static SimpleDateFormat sharedFormatter = new SimpleDateFormat("yyyy-MM-dd");

    // Correct approach: DateTimeFormatter is immutable and always safe to share
    static final DateTimeFormatter SAFE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    void main() {
        // Bug 1: LocalDateTime for audit timestamps — no timezone, ambiguous globally
        LocalDateTime auditTime = LocalDateTime.now();
        LOG.info("Bug - LocalDateTime audit timestamp (no timezone): " + auditTime);

        // Correct: Instant for audit timestamps — always UTC, unambiguous everywhere
        Instant auditInstant = Instant.now();
        LOG.info("Correct - Instant audit timestamp (UTC): " + auditInstant);

        // Bug 2: Storing user appointment as Instant — loses meaning across DST transitions
        Instant appointmentWrong = Instant.now();
        LOG.info("Bug - Appointment as Instant (DST-blind): " + appointmentWrong);

        // Correct: ZonedDateTime preserves timezone as part of the appointment data
        ZonedDateTime appointmentRight =
                ZonedDateTime.of(2024, 6, 18, 14, 0, 0, 0,
                        ZoneId.of("Asia/Kolkata"));
        LOG.info("Correct - Appointment as ZonedDateTime: " + appointmentRight);

        // Bug 3: Hardcoded UTC offset — London is UTC+0 in winter, UTC+1 in summer
        ZoneOffset hardcoded = ZoneOffset.of("+01:00");
        LOG.info("Bug - Hardcoded offset (ignores DST): " + hardcoded);

        // Correct: ZoneId encodes DST rules — offset is computed from the rules
        ZoneId london = ZoneId.of("Europe/London");
        LOG.info("Correct - ZoneId with DST rules: " + london);

        // Demonstrate SAFE_FORMATTER shared across calls
        String formatted = LocalDateTime.now().format(SAFE_FORMATTER);
        LOG.info("Safe shared formatter result: " + formatted);

        // Output:
        // Bug - LocalDateTime audit timestamp (no timezone): 2024-06-18T14:00:00.123
        // Correct - Instant audit timestamp (UTC): 2024-06-18T08:30:00.456Z
        // Bug - Appointment as Instant (DST-blind): 2024-06-18T08:30:00.456Z
        // Correct - Appointment as ZonedDateTime: 2024-06-18T14:00+05:30[Asia/Kolkata]
        // Bug - Hardcoded offset (ignores DST): +01:00
        // Correct - ZoneId with DST rules: Europe/London
        // Safe shared formatter result: 2024-06-18
    }
}