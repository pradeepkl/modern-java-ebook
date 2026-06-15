// Java 25+
// Feature shown: LocalDate, LocalTime, LocalDateTime, Instant, final in Java 8+

/**
 * Listing 9.3 — HumanVsMachineTime.java
 * Demonstrates: The distinction between human time (LocalDate, LocalTime,
 * LocalDateTime) and machine time (Instant) in the java.time API.
 * Chapter 9: Modern Date and Time
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter09;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Logger;

public class HumanVsMachineTime {

    private static final Logger LOG =
            Logger.getLogger(HumanVsMachineTime.class.getName());

    void main() {

        // HUMAN TIME — dates and times as people experience them

        // A birthday — repeats every year, no timezone
        LocalDate birthday = LocalDate.of(1990, 4, 12);

        // A store opening time — same wall clock time every day
        LocalTime openingTime = LocalTime.of(9, 0);

        // A scheduled appointment — date and time, no timezone
        // "Tuesday at 3pm" means the same regardless of
        // which city the calendar app is running in
        LocalDateTime appointment =
                LocalDateTime.of(2024, 6, 18, 15, 0);

        LOG.info("Birthday:    " + birthday);
        LOG.info("Opening:     " + openingTime);
        LOG.info("Appointment: " + appointment);

        // MACHINE TIME — precise moments, universally unambiguous

        // A payment timestamp — this happened at this instant
        // Every system in the world agrees on this moment
        Instant paymentProcessed = Instant.now();

        // An audit log entry
        Instant recordCreated = Instant.now();

        // A message send time
        Instant messageSent = Instant.parse(
                "2024-06-18T10:15:30Z");

        // The Z suffix means UTC — machine time is always UTC

        LOG.info("Payment processed: " + paymentProcessed);
        LOG.info("Record created:    " + recordCreated);
        LOG.info("Message sent:      " + messageSent);

        // Output:
        // Birthday:    1990-04-12
        // Opening:     09:00
        // Appointment: 2024-06-18T15:00
        // Payment processed: 2024-06-18T10:15:30.123456789Z
        // Record created:    2024-06-18T10:15:30.123456790Z
        // Message sent:      2024-06-18T10:15:30Z
    }
}