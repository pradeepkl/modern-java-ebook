// Java 25+
// Feature shown: ZoneId and ZoneOffset for non-whole-hour offsets, final in Java 8+

/**
 * Listing 9.1b — ISTOffset.java
 * Demonstrates: ZoneId and ZoneOffset for non-whole-hour UTC offsets (IST = UTC+05:30)
 * Chapter 9: Modern Date and Time
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter09;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

public class ISTOffset {

    private static final Logger log = Logger.getLogger(ISTOffset.class.getName());

    void main() {
        // Obtain the Asia/Kolkata zone — IST is UTC+05:30
        ZoneId ist = ZoneId.of("Asia/Kolkata");
        ZonedDateTime istNow = ZonedDateTime.now(ist);

        // IST offset is +05:30 — not +05:00 or +06:00
        // ZoneOffset correctly represents this fractional-hour offset
        ZoneOffset offset = istNow.getOffset();
        log.info("IST offset: " + offset); // +05:30

        // Naive offset assumption — wrong for IST
        // int localHour = utcHour + 5; // misses the 30-minute component

        // Correct approach: always use ZoneId — never manual arithmetic
        ZonedDateTime utcTime = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime istTime = utcTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));

        log.info("UTC time : " + utcTime);
        log.info("IST time : " + istTime);

        // Verify the offset is exactly +05:30 (19800 seconds)
        int totalSeconds = offset.getTotalSeconds(); // 19800 = 5*3600 + 30*60
        log.info("Offset total seconds: " + totalSeconds);

        // Confirm both represent the same instant
        boolean sameInstant = utcTime.toInstant().equals(istTime.toInstant());
        log.info("Same instant: " + sameInstant); // true

        // Output:
        // IST offset: +05:30
        // UTC time : 2024-03-15T10:00:00Z[UTC]
        // IST time : 2024-03-15T15:30:00+05:30[Asia/Kolkata]
        // Offset total seconds: 19800
        // Same instant: true
    }
}