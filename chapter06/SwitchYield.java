// Java 25+
// Feature shown: switch expressions with yield, final in Java 14+

/**
 * Listing 6.6 — SwitchYield.java
 * Demonstrates: switch expressions using yield to return a value from a block arm
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 14+ for switch expressions; Java 25+ for void main()
 * instance main method (JEP 512)
 */
package chapter06;

import java.util.logging.Logger;

public class SwitchYield {

    private static final Logger log =
            Logger.getLogger(SwitchYield.class.getName());

    enum Priority { LOW, MEDIUM, HIGH, CRITICAL }

    // Returns SLA in minutes; CRITICAL arm uses a block with yield
    public static int slaMinutes(Priority priority) {
        return switch (priority) {
            case LOW      -> 480;   // single-expression arm, no yield needed
            case MEDIUM   -> 120;
            case HIGH     -> 30;
            case CRITICAL -> {
                // Block arm: side effects allowed before yielding the value
                log.warning("Critical SLA breach risk — escalating");
                yield 5;            // yield returns the value from the block
            }
        };
    }

    void main() {
        // Exercise every arm of the switch expression
        for (Priority p : Priority.values()) {
            int sla = slaMinutes(p);
            log.info("Priority " + p + " -> SLA " + sla + " minutes");
        }

        // Output:
        // WARNING: Critical SLA breach risk — escalating
        // INFO: Priority LOW     -> SLA 480 minutes
        // INFO: Priority MEDIUM  -> SLA 120 minutes
        // INFO: Priority HIGH    -> SLA 30 minutes
        // INFO: Priority CRITICAL -> SLA 5 minutes
    }
}