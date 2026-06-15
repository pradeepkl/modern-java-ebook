// Java 25+
// Feature shown: explicit ZoneId in cloud containers, final in Java 8+
/**
 * Listing 9.6b — ContainerTimezone.java
 * Demonstrates: Why cloud containers require explicit ZoneId usage
 * Chapter 9: Modern Date and Time
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter09;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

public class ContainerTimezone {

    private static final Logger LOG =
            Logger.getLogger(ContainerTimezone.class.getName());

    void main() {

        // NOT IDEAL: Uses JVM default timezone — unpredictable in containers
        // In a Docker container this is often UTC regardless of user locale
        ZonedDateTime dangerous = ZonedDateTime.now();
        LOG.info("Dangerous (JVM default zone): " + dangerous);

        // Correct approach: Always specify the timezone explicitly
        // The zone is business data — make it visible in the code
        ZonedDateTime safe = ZonedDateTime.now(
                ZoneId.of("Asia/Kolkata"));
        LOG.info("Safe (explicit Asia/Kolkata): " + safe);

        // Correct approach: For machine timestamps — always UTC
        // Instant.now() is always UTC — no zone ambiguity possible
        Instant machineTime = Instant.now();
        LOG.info("Machine timestamp (UTC Instant): " + machineTime);

        // Demonstrate the zone difference clearly
        ZoneId kolkata = ZoneId.of("Asia/Kolkata");
        ZoneId utc     = ZoneId.of("UTC");

        // Same instant, two different zone representations
        ZonedDateTime inKolkata = machineTime.atZone(kolkata);
        ZonedDateTime inUtc     = machineTime.atZone(utc);

        LOG.info("Same instant in Asia/Kolkata : " + inKolkata);
        LOG.info("Same instant in UTC          : " + inUtc);

        // Confirm both represent the identical point in time
        boolean sameInstant = inKolkata.toInstant()
                .equals(inUtc.toInstant());
        LOG.info("Both represent the same instant: " + sameInstant);

        // Output:
        // Dangerous (JVM default zone): 2024-06-18T09:30+01:00[Europe/London]
        // Safe (explicit Asia/Kolkata): 2024-06-18T14:00+05:30[Asia/Kolkata]
        // Machine timestamp (UTC Instant): 2024-06-18T08:30:00.123456Z
        // Same instant in Asia/Kolkata : 2024-06-18T14:00+05:30[Asia/Kolkata]
        // Same instant in UTC          : 2024-06-18T08:30+00:00[UTC]
        // Both represent the same instant: true
    }
}