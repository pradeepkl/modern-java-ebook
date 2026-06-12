// Java 8+
/**
 * Listing 9.3 — HumanVsMachineTime.java
 * Demonstrates: The distinction between human time (LocalDate, LocalTime,
 *               LocalDateTime) and machine time (Instant)
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+
 */
package chapter09;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Logger;

public class HumanVsMachineTime {

    private static final Logger LOG = Logger.getLogger(HumanVsMachineTime.class.getName());

    public static void main(String[] args) {

        // --- HUMAN TIME: dates and times as people experience them ---

        // A birthday — repeats every year, no timezone needed
        LocalDate birthday = LocalDate.of(1990, 4, 12);
        LOG.info("Birthday (LocalDate): " + birthday);

        // A store opening time — same wall-clock time every day
        LocalTime openingTime = LocalTime.of(9, 0);
        LOG.info("Opening time (LocalTime): " + openingTime);

        // A scheduled appointment — date and time, no timezone
        // "Tuesday at 3pm" means the same regardless of which city
        // the calendar app is running in
        LocalDateTime appointment = LocalDateTime.of(2024, 6, 18, 15, 0);
        LOG.info("Appointment (LocalDateTime): " + appointment);

        // --- MACHINE TIME: precise moments, universally unambiguous ---

        // A payment timestamp — this happened at this exact instant
        // Every system in the world agrees on this moment
        Instant paymentProcessed = Instant.now();
        LOG.info("Payment processed (Instant): " + paymentProcessed);

        // An audit log entry — captured at a precise point in time
        Instant recordCreated = Instant.now();
        LOG.info("Record created (Instant): " + recordCreated);

        // A message send time — parsed from ISO-8601 UTC string
        // The Z suffix means UTC — machine time is always UTC
        Instant messageSent = Instant.parse("2024-06-18T10:15:30Z");
        LOG.info("Message sent (Instant): " + messageSent);

        // Key rule: LocalDateTime has NO timezone — never use it for audit logs
        LOG.info("--- Summary ---");
        LOG.info("Human time types: LocalDate, LocalTime, LocalDateTime");
        LOG.info("Machine time type: Instant (always UTC, always unambiguous)");

        // Output:
        // Birthday (LocalDate): 1990-04-12
        // Opening time (LocalTime): 09:00
        // Appointment (LocalDateTime): 2024-06-18T15:00
        // Payment processed (Instant): 2024-06-18T10:15:30.123456789Z  (varies)
        // Record created (Instant): 2024-06-18T10:15:30.234567890Z      (varies)
        // Message sent (Instant): 2024-06-18T10:15:30Z
    }
}