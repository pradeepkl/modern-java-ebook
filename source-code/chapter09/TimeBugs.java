// Java 8+
/**
 * Listing 9.12 — TimeBugs.java
 * Demonstrates: Common date/time bugs and their correct alternatives
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+
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

    private static final Logger log = Logger.getLogger(TimeBugs.class.getName());

    // NOT IDEAL: Shared SimpleDateFormat — not thread-safe
    static SimpleDateFormat sharedFormatter =
            new SimpleDateFormat("yyyy-MM-dd");
    // Multiple threads calling sharedFormatter.format()
    // simultaneously will corrupt each other's output

    // Correct approach: DateTimeFormatter — immutable, always safe to share
    static final DateTimeFormatter SAFE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void demonstrateBugs() {
        // Bug 1: LocalDateTime for audit timestamps — no timezone
        LocalDateTime auditTime = LocalDateTime.now();
        log.info("Ambiguous audit time (no zone): " + auditTime);

        // Correct: Instant for audit timestamps — always UTC
        Instant auditInstant = Instant.now();
        log.info("Unambiguous audit instant (UTC): " + auditInstant);

        // Bug 2: Storing user appointment as Instant loses DST meaning
        Instant appointmentWrong = Instant.now();
        log.info("Wrong appointment (Instant loses wall-clock meaning): " + appointmentWrong);

        // Correct: ZonedDateTime preserves timezone as part of the data
        ZonedDateTime appointmentRight =
                ZonedDateTime.of(2024, 6, 18, 14, 0, 0, 0,
                        ZoneId.of("Asia/Kolkata"));
        log.info("Correct appointment (ZonedDateTime): " + appointmentRight);

        // Bug 3: Hardcoded UTC offset — wrong for DST regions
        ZoneOffset hardcoded = ZoneOffset.of("+01:00");
        log.info("Hardcoded offset (wrong half the year): " + hardcoded);

        // Correct: ZoneId knows DST rules automatically
        ZoneId london = ZoneId.of("Europe/London");
        log.info("Correct zone with DST rules: " + london);

        // Safe formatter is thread-safe; sharedFormatter is not
        log.info("Safe formatted date: " +
                appointmentRight.toLocalDate().format(SAFE_FORMATTER));
    }

    public static void main(String[] args) {
        demonstrateBugs();
        // Output:
        // Ambiguous audit time (no zone): 2024-06-18T14:00:00.123
        // Unambiguous audit instant (UTC): 2024-06-18T08:30:00.456Z
        // Wrong appointment (Instant loses wall-clock meaning): 2024-06-18T08:30:00.456Z
        // Correct appointment (ZonedDateTime): 2024-06-18T14:00+05:30[Asia/Kolkata]
        // Hardcoded offset (wrong half the year): +01:00
        // Correct zone with DST rules: Europe/London
        // Safe formatted date: 2024-06-18
    }
}