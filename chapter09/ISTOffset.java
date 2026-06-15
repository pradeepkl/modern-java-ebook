// Java 25+
// Feature shown: ZoneId and ZoneOffset for non-whole-hour offsets, final in Java 8+

/**
 * Listing 9.1b — ISTOffset.java
 * Demonstrates: ZoneId and ZoneOffset handling for non-whole-hour UTC offsets
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

    private static final Logger LOG = Logger.getLogger(ISTOffset.class.getName());

    void main() {
        // Obtain the Asia/Kolkata zone — IST is UTC+05:30
        ZoneId ist = ZoneId.of("Asia/Kolkata");
        ZonedDateTime istNow = ZonedDateTime.now(ist);

        // IST offset is +05:30 — not +05:00 or +06:00
        // ZoneOffset correctly represents this fractional-hour offset
        ZoneOffset offset = istNow.getOffset();
        LOG.info("IST offset: " + offset); // +05:30

        // Naive offset assumption — wrong for IST
        // int localHour = utcHour + 5; // misses the 30-minute component

        // Correct approach: use ZoneId and withZoneSameInstant — never manual arithmetic
        ZonedDateTime utcTime = ZonedDateTime.now(
                ZoneId.of("UTC"));
        ZonedDateTime istTime = utcTime.withZoneSameInstant(
                ZoneId.of("Asia/Kolkata"));

        LOG.info("UTC time : " + utcTime);
        LOG.info("IST time : " + istTime);

        // Verify the offset is exactly 5 hours and 30 minutes (19800 seconds)
        int offsetSeconds = istTime.getOffset().getTotalSeconds();
        LOG.info("Offset total seconds: " + offsetSeconds); // 19800

        boolean isHalfHourOffset = (offsetSeconds % 3600) != 0;
        LOG.info("IST has a non-whole-hour offset: " + isHalfHourOffset); // true

        // Output:
        // IST offset: +05:30
        // UTC time : 2024-03-15T10:00:00Z[UTC]
        // IST time : 2024-03-15T15:30:00+05:30[Asia/Kolkata]
        // Offset total seconds: 19800
        // IST has a non-whole-hour offset: true
    }
}