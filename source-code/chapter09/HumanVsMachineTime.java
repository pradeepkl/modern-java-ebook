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

    private static final Logger LOG =
            Logger.getLogger(HumanVsMachineTime.class.getName());

    public static void main(String[] args) {

        // --- HUMAN TIME: dates and times as people experience them ---

        // A birthday — repeats every year, no timezone needed
        LocalDate birthday = LocalDate.of(1990, 4, 12);

        // A store opening time — same wall-clock time every day
        LocalTime openingTime = LocalTime.of(9, 0);

        // A scheduled appointment — date and time, no timezone
        // "Tuesday at 3pm" means the same regardless of
        // which city the calendar app is running in
        LocalDateTime appointment = LocalDateTime.of(2024, 6, 18, 15, 0);

        LOG.info("=== HUMAN TIME ===");
        LOG.info("Birthday        : " + birthday);       // 1990-04-12
        LOG.info("Opening time    : " + openingTime);    // 09:00
        LOG.info("Appointment     : " + appointment);    // 2024-06-18T15:00

        // --- MACHINE TIME: precise moments, universally unambiguous ---

        // A payment timestamp — this happened at this exact instant
        // Every system in the world agrees on this moment
        Instant paymentProcessed = Instant.now();

        // An audit log entry — captured at this nanosecond
        Instant recordCreated = Instant.now();

        // A message send time — parsed from ISO-8601 UTC string
        // The Z suffix means UTC — machine time is always UTC
        Instant messageSent = Instant.parse("2024-06-18T10:15:30Z");

        LOG.info("=== MACHINE TIME ===");
        LOG.info("Payment processed : " + paymentProcessed);
        LOG.info("Record created    : " + recordCreated);
        LOG.info("Message sent      : " + messageSent);  // 2024-06-18T10:15:30Z

        // Output:
        // === HUMAN TIME ===
        // Birthday        : 1990-04-12
        // Opening time    : 09:00
        // Appointment     : 2024-06-18T15:00
        // === MACHINE TIME ===
        // Payment processed : 2024-06-18T08:23:45.123456789Z  (current instant)
        // Record created    : 2024-06-18T08:23:45.123456790Z  (current instant)
        // Message sent      : 2024-06-18T10:15:30Z
    }
}