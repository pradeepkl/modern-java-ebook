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
        log.info("BAD  audit timestamp (no zone): " + auditTime);

        // Correct: Instant for audit timestamps — always UTC
        Instant auditInstant = Instant.now();
        log.info("GOOD audit timestamp (UTC Instant): " + auditInstant);

        // Bug 2: Storing user appointment as Instant — loses DST meaning
        Instant appointmentWrong = Instant.now();
        log.info("BAD  appointment (Instant loses DST context): " + appointmentWrong);

        // Correct: ZonedDateTime preserves timezone rules including DST
        ZonedDateTime appointmentRight =
                ZonedDateTime.of(2024, 6, 18, 14, 0, 0, 0,
                        ZoneId.of("Asia/Kolkata"));
        log.info("GOOD appointment (ZonedDateTime): " + appointmentRight);

        // Bug 3: Hardcoded UTC offset — wrong when DST changes
        ZoneOffset hardcoded = ZoneOffset.of("+01:00");
        log.info("BAD  London offset hardcoded: " + hardcoded
                + " (wrong in winter — London is UTC+0 then)");

        // Correct: ZoneId knows DST rules automatically
        ZoneId london = ZoneId.of("Europe/London");
        log.info("GOOD London ZoneId (DST-aware): " + london);
    }

    public static void main(String[] args) {
        demonstrateBugs();
        // Output:
        // BAD  audit timestamp (no zone): 2024-06-18T14:00:00.123
        // GOOD audit timestamp (UTC Instant): 2024-06-18T08:30:00.456Z
        // BAD  appointment (Instant loses DST context): 2024-06-18T08:30:00.456Z
        // GOOD appointment (ZonedDateTime): 2024-06-18T14:00+05:30[Asia/Kolkata]
        // BAD  London offset hardcoded: +01:00 (wrong in winter — London is UTC+0 then)
        // GOOD London ZoneId (DST-aware): Europe/London
    }
}