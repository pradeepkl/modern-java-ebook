// Java 8+
package chapter07;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Listing 7.6 — UnboxingNullPointer.java
 * Demonstrates: NullPointerException from silent unboxing of absent map values,
 *               and safe alternatives using getOrDefault and requireNonNullElse.
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 8+
 */
public class UnboxingNullPointer {

    private static final Logger log =
            Logger.getLogger(UnboxingNullPointer.class.getName());

    public static void processOrder(
            Map<String, Integer> discounts,
            String customerId) {

        // ✅ Safe — explicit fallback when key is absent
        int safeDiscount = discounts.getOrDefault(customerId, 0);

        // ✅ Safe — requireNonNullElse provides explicit fallback
        // discounts.get() returns Integer (nullable); unboxing is guarded
        int guarded = Objects.requireNonNullElse(
                discounts.get(customerId), 0);

        log.info("Customer: " + customerId);
        log.info("Safe (getOrDefault): " + safeDiscount);
        log.info("Guarded (requireNonNullElse): " + guarded);
    }

    public static void demonstrateNpe(
            Map<String, Integer> discounts,
            String customerId) {
        try {
            // discounts.get() returns Integer — null if key absent.
            // Compiler silently inserts .intValue() for unboxing.
            // → NullPointerException when customerId is not in map
            int discount = discounts.get(customerId); // ❌ unsafe unboxing
            log.info("Discount: " + discount);
        } catch (NullPointerException e) {
            // Caught to demonstrate the runtime failure
            log.warning("NullPointerException during unboxing for key: "
                    + customerId);
        }
    }

    public static void main(String[] args) {
        Map<String, Integer> discounts = new HashMap<>();
        discounts.put("CUST-001", 15); // known customer with discount

        // Known customer — all paths return 15
        processOrder(discounts, "CUST-001");

        // Unknown customer — safe paths return 0, unsafe throws NPE
        processOrder(discounts, "CUST-999");
        demonstrateNpe(discounts, "CUST-999");

        // Output:
        // INFO: Customer: CUST-001
        // INFO: Safe (getOrDefault): 15
        // INFO: Guarded (requireNonNullElse): 15
        // INFO: Customer: CUST-999
        // INFO: Safe (getOrDefault): 0
        // INFO: Guarded (requireNonNullElse): 0
        // WARNING: NullPointerException during unboxing for key: CUST-999
    }
}