// Java 8+
/**
 * Listing 9.9 — FormattingAndParsing.java
 * Demonstrates: DateTimeFormatter formatting, parsing, and flexible multi-format parsing
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+
 */
package chapter09;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

public class FormattingAndParsing {

    private static final Logger log =
            Logger.getLogger(FormattingAndParsing.class.getName());

    // Static formatters — immutable, thread-safe, sharable
    private static final DateTimeFormatter ISO_DATE =
            DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter CUSTOM =
            DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter WITH_ZONE =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm z");

    // Flexible formatter accepts multiple input formats
    private static final DateTimeFormatter FLEXIBLE =
            new DateTimeFormatterBuilder()
                    .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    .appendOptional(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                    .toFormatter();

    public static void main(String[] args) {
        // Formatting a LocalDate
        LocalDate date = LocalDate.of(2024, 6, 18);
        String iso   = date.format(ISO_DATE);   // 2024-06-18
        String human = date.format(CUSTOM);     // 18 Jun 2024
        log.info("ISO format:    " + iso);
        log.info("Human format:  " + human);

        // Formatting a ZonedDateTime with zone abbreviation
        ZonedDateTime zdt = ZonedDateTime.of(
                date.atTime(14, 30), ZoneId.of("Asia/Kolkata"));
        String full = zdt.format(WITH_ZONE);    // 18 Jun 2024 14:30 IST
        log.info("Zoned format:  " + full);

        // Parsing — always wrap in try-catch; user input is unreliable
        try {
            LocalDate parsed = LocalDate.parse("2024-06-18", ISO_DATE);
            log.info("Parsed date:   " + parsed);
        } catch (DateTimeParseException e) {
            log.warning("Invalid date format: " + e.getMessage());
        }

        // Flexible parsing — all three formats succeed
        LocalDate a = LocalDate.parse("2024-06-18",  FLEXIBLE);
        LocalDate b = LocalDate.parse("18/06/2024",  FLEXIBLE);
        LocalDate c = LocalDate.parse("18 Jun 2024", FLEXIBLE);
        log.info("Flexible a:    " + a);
        log.info("Flexible b:    " + b);
        log.info("Flexible c:    " + c);

        // Output:
        // ISO format:    2024-06-18
        // Human format:  18 Jun 2024
        // Zoned format:  18 Jun 2024 14:30 IST
        // Parsed date:   2024-06-18
        // Flexible a:    2024-06-18
        // Flexible b:    2024-06-18
        // Flexible c:    2024-06-18
    }
}