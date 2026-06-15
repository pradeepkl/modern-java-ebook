// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 7.6 — UnboxingNullPointer.java
 * Demonstrates: Unboxing NullPointerException risk and safe fallback patterns
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter07;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class UnboxingNullPointer {

    private static final Logger log =
            Logger.getLogger(UnboxingNullPointer.class.getName());

    // Demonstrates the unboxing NPE trap and two safe alternatives
    static void processOrder(
            Map<String, Integer> discounts,
            String customerId) {

        // Safe approach: getOrDefault avoids null entirely
        // Returns 0 when the key is absent — no unboxing of null
        int safeDiscount = discounts.getOrDefault(customerId, 0);

        // Safe approach: requireNonNullElse guards against null explicitly
        // discounts.get() may return null; fallback is 0
        int guarded = Objects.requireNonNullElse(
                discounts.get(customerId), 0);

        log.info("Customer: " + customerId);
        log.info("Safe (getOrDefault): " + safeDiscount);
        log.info("Guarded (requireNonNullElse): " + guarded);
    }

    void main() {
        Map<String, Integer> discounts = new HashMap<>();
        discounts.put("CUST-001", 15); // 15 percent discount
        discounts.put("CUST-002", 0);  // explicitly zero discount

        // Known customer — discount present in map
        processOrder(discounts, "CUST-001");

        // Unknown customer — key absent, would NPE on raw unboxing
        processOrder(discounts, "CUST-999");

        // Demonstrate the NPE that raw unboxing would cause
        try {
            // discounts.get() returns null for absent key
            // The compiler generates .intValue() — throws NullPointerException
            int unsafe = discounts.get("CUST-999"); // NOT IDEAL
            log.info("Unsafe discount: " + unsafe);
        } catch (NullPointerException e) {
            log.warning("NullPointerException caught: unboxing null Integer to int");
        }

        // Output:
        // INFO: Customer: CUST-001
        // INFO: Safe (getOrDefault): 15
        // INFO: Guarded (requireNonNullElse): 15
        // INFO: Customer: CUST-999
        // INFO: Safe (getOrDefault): 0
        // INFO: Guarded (requireNonNullElse): 0
        // WARNING: NullPointerException caught: unboxing null Integer to int
    }
}