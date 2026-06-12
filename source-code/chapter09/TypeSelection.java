// Java 8+
/**
 * Listing 9.4 — TypeSelection.java
 * Demonstrates: Choosing the right java.time type for each concept
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+
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

    private static final Logger LOG = Logger.getLogger(TypeSelection.class.getName());

    public static void main(String[] args) {

        // LocalDate — date without time or timezone
        LocalDate invoiceDueDate = LocalDate.of(2024, 12, 31);
        LocalDate today = LocalDate.now();
        boolean isOverdue = today.isAfter(invoiceDueDate); // compare calendar dates
        LOG.info("Invoice due: " + invoiceDueDate + " | Overdue: " + isOverdue);

        // LocalTime — time without date or timezone
        LocalTime shopCloses = LocalTime.of(21, 30);
        LocalTime now = LocalTime.now();
        boolean isOpen = now.isBefore(shopCloses); // wall-clock comparison
        LOG.info("Shop closes: " + shopCloses + " | Currently open: " + isOpen);

        // Instant — machine timestamp, always UTC
        Instant orderCreated = Instant.now();
        Instant orderShipped = Instant.now().plusSeconds(3600); // 1 hour later
        Duration processingTime = Duration.between(orderCreated, orderShipped);
        LOG.info("Processing time (minutes): " + processingTime.toMinutes());

        // ZonedDateTime — date-time with full timezone
        ZonedDateTime globalMeeting = ZonedDateTime.of(
                2024, 6, 18, 14, 0, 0, 0,
                ZoneId.of("Europe/London"));
        ZonedDateTime mumbaiView = globalMeeting
                .withZoneSameInstant(ZoneId.of("Asia/Kolkata")); // same instant, different zone
        LOG.info("London meeting: " + globalMeeting.toLocalTime());
        LOG.info("Mumbai view:    " + mumbaiView.toLocalTime()); // London 14:00 = Mumbai 19:30

        // MonthDay — recurring annual event (no year)
        MonthDay companyFoundingDay = MonthDay.of(3, 15); // March 15 every year
        MonthDay today2 = MonthDay.now();
        boolean isAnniversary = today2.equals(companyFoundingDay);
        LOG.info("Founding day: " + companyFoundingDay + " | Anniversary today: " + isAnniversary);

        // YearMonth — billing periods
        YearMonth billingPeriod = YearMonth.of(2024, 6); // June 2024
        int daysInPeriod = billingPeriod.lengthOfMonth(); // 30 days in June
        LOG.info("Billing period: " + billingPeriod + " | Days: " + daysInPeriod);

        // Output:
        // Invoice due: 2024-12-31 | Overdue: true
        // Shop closes: 21:30 | Currently open: true
        // Processing time (minutes): 60
        // London meeting: 14:00
        // Mumbai view:    19:30
        // Founding day: --03-15 | Anniversary today: false
        // Billing period: 2024-06 | Days: 30
    }
}