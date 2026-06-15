// Java 25+
// Feature shown: DateTimeFormatter, DateTimeFormatterBuilder, final in Java 8+

/**
 * Listing 9.9 — FormattingAndParsing.java
 * Demonstrates: DateTimeFormatter for formatting and parsing, and
 * DateTimeFormatterBuilder for flexible multi-pattern parsing
 * Chapter 9: Modern Date and Time
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
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

    void main() {
        Logger log = Logger.getLogger("FormattingAndParsing");

        // DateTimeFormatter — immutable, thread-safe, sharable as static fields
        DateTimeFormatter isoDate = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter custom  = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter withZone = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm z");

        // Formatting a LocalDate
        LocalDate date = LocalDate.of(2024, 6, 18);
        String iso   = date.format(isoDate); // 2024-06-18
        String human = date.format(custom);  // 18 Jun 2024
        log.info("ISO:   " + iso);
        log.info("Human: " + human);

        // Formatting a ZonedDateTime with zone abbreviation
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        String full = zdt.format(withZone);  // e.g. 18 Jun 2024 14:30 IST
        log.info("Full:  " + full);

        // Parsing — always wrap in try-catch; user input is unreliable
        try {
            LocalDate parsed = LocalDate.parse(
                    "2024-06-18", DateTimeFormatter.ISO_LOCAL_DATE);
            log.info("Parsed: " + parsed);
        } catch (DateTimeParseException e) {
            // Handle invalid format — do not propagate raw exception
            log.warning("Invalid date format: " + e.getMessage());
        }

        // DateTimeFormatterBuilder — accept multiple formats from the same source
        DateTimeFormatter flexible = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .appendOptional(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                .toFormatter();

        // All three formats parse successfully to the same LocalDate
        LocalDate a = LocalDate.parse("2024-06-18",  flexible);
        LocalDate b = LocalDate.parse("18/06/2024",  flexible);
        LocalDate c = LocalDate.parse("18 Jun 2024", flexible);
        log.info("a=" + a + "  b=" + b + "  c=" + c);

        // Output:
        // INFO: ISO:   2024-06-18
        // INFO: Human: 18 Jun 2024
        // INFO: Full:  18 Jun 2024 14:30 IST
        // INFO: Parsed: 2024-06-18
        // INFO: a=2024-06-18  b=2024-06-18  c=2024-06-18
    }
}