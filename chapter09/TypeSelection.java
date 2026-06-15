// Java 25+
// Feature shown: java.time type selection (LocalDate, Instant, ZonedDateTime, MonthDay, YearMonth), final in Java 8+

/**
 * Listing 9.4 — TypeSelection.java
 * Demonstrates: Choosing the right java.time type for each domain concept
 * Chapter 9: Modern Date and Time
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter09;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

public class TypeSelection {

    private static final Logger log = Logger.getLogger(TypeSelection.class.getName());

    void main() {
        // LocalDate — date without time or timezone
        LocalDate invoiceDueDate = LocalDate.of(2024, 12, 31);
        LocalDate today = LocalDate.now();
        boolean isOverdue = today.isAfter(invoiceDueDate);
        log.info("Invoice due: " + invoiceDueDate + ", overdue: " + isOverdue);

        // LocalTime — time without date or timezone
        LocalTime shopCloses = LocalTime.of(21, 30);
        LocalTime now = LocalTime.now();
        boolean isOpen = now.isBefore(shopCloses);
        log.info("Shop closes: " + shopCloses + ", currently open: " + isOpen);

        // Instant — machine timestamp, always UTC
        Instant orderCreated = Instant.now();
        Instant orderShipped = Instant.now().plusSeconds(3600);
        Duration processingTime = Duration.between(orderCreated, orderShipped);
        log.info("Processing time (seconds): " + processingTime.getSeconds());

        // ZonedDateTime — date-time with full timezone
        ZonedDateTime globalMeeting = ZonedDateTime.of(
                2024, 6, 18, 14, 0, 0, 0,
                ZoneId.of("Europe/London"));
        ZonedDateTime mumbaiView = globalMeeting
                .withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
        // London 14:00 = Mumbai 19:30
        log.info("London meeting: " + globalMeeting.toLocalTime()
                + ", Mumbai view: " + mumbaiView.toLocalTime());

        // MonthDay — recurring annual event
        MonthDay companyFoundingDay = MonthDay.of(3, 15); // March 15 every year
        MonthDay today2 = MonthDay.now();
        boolean isAnniversary = today2.equals(companyFoundingDay);
        log.info("Founding day: " + companyFoundingDay + ", anniversary today: " + isAnniversary);

        // YearMonth — billing periods
        YearMonth billingPeriod = YearMonth.of(2024, 6); // June 2024
        int daysInPeriod = billingPeriod.lengthOfMonth(); // 30
        log.info("Billing period: " + billingPeriod + ", days: " + daysInPeriod);

        // Output:
        // Invoice due: 2024-12-31, overdue: true
        // Shop closes: 21:30, currently open: true
        // Processing time (seconds): 3600
        // London meeting: 14:00, Mumbai view: 19:30
        // Founding day: --03-15, anniversary today: false
        // Billing period: 2024-06, days: 30
    }
}