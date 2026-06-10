// Java 14+
/**
 * Listing 6.6 — SwitchYield.java
 * Demonstrates: Using yield to return a value from a switch expression block
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 14+
 */
package chapter06;

import java.util.logging.Logger;

public class SwitchYield {

    private static final Logger log =
            Logger.getLogger(SwitchYield.class.getName());

    enum Priority { LOW, MEDIUM, HIGH, CRITICAL }

    // Returns SLA in minutes based on priority level
    public static int slaMinutes(Priority priority) {
        return switch (priority) {
            case LOW      -> 480;   // 8 hours
            case MEDIUM   -> 120;   // 2 hours
            case HIGH     -> 30;    // 30 minutes
            case CRITICAL -> {
                // Block arm: multiple statements allowed
                log.warning(
                    "Critical SLA breach risk — escalating");
                yield 5;  // yield returns value from block
            }
        };
    }

    public static void main(String[] args) {
        // Demonstrate each priority level and its SLA
        for (Priority p : Priority.values()) {
            int sla = slaMinutes(p);
            log.info("Priority " + p + " -> SLA: " + sla + " minutes");
        }

        // Output:
        // WARNING: Critical SLA breach risk — escalating
        // INFO: Priority LOW     -> SLA: 480 minutes
        // INFO: Priority MEDIUM  -> SLA: 120 minutes
        // INFO: Priority HIGH    -> SLA: 30 minutes
        // INFO: Priority CRITICAL -> SLA: 5 minutes
    }
}