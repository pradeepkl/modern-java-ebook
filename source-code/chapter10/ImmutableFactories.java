// Java 9+
/**
 * Listing 10.6 — ImmutableFactories.java
 * Demonstrates: List.of, Set.of, Map.of, and Map.ofEntries factory methods
 * Chapter 10: Collections, Ownership, and State Safety
 * Requires: Java 9+
 */
package chapter10;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ImmutableFactories {

    private static final Logger log =
            Logger.getLogger(ImmutableFactories.class.getName());

    public static void main(String[] args) {

        // List.of — ordered, allows duplicates, immutable
        List<String> statuses = List.of(
                "PENDING", "CONFIRMED",
                "SHIPPED", "DELIVERED");

        // Set.of — unordered, no duplicates, immutable
        Set<String> validCurrencies = Set.of(
                "GBP", "USD", "EUR", "JPY");

        // Map.of — up to 10 entries, immutable
        Map<String, Integer> slaMinutes = Map.of(
                "STANDARD",  480,
                "EXPRESS",    60,
                "OVERNIGHT",  720);

        // Map.ofEntries — for more than 10 entries
        Map<String, String> regionCodes =
                Map.ofEntries(
                        Map.entry("UK", "GBP"),
                        Map.entry("US", "USD"),
                        Map.entry("DE", "EUR"),
                        Map.entry("JP", "JPY"),
                        Map.entry("AU", "AUD"));

        // Modification throws UnsupportedOperationException
        try {
            statuses.add("CANCELLED"); // structurally immutable — throws
        } catch (UnsupportedOperationException e) {
            log.info("Cannot modify — structurally immutable");
        }

        // Null elements rejected at construction time
        try {
            List<String> withNull =
                    List.of("PENDING", null, "SHIPPED"); // throws NPE
        } catch (NullPointerException e) {
            log.info("Null rejected — use Optional for absent values");
        }

        log.info("Statuses: " + statuses);
        log.info("Valid currencies: " + validCurrencies);
        log.info("SLA for EXPRESS: " + slaMinutes.get("EXPRESS"));
        log.info("Currency for AU: " + regionCodes.get("AU"));

        // Output:
        // INFO: Cannot modify — structurally immutable
        // INFO: Null rejected — use Optional for absent values
        // INFO: Statuses: [PENDING, CONFIRMED, SHIPPED, DELIVERED]
        // INFO: Valid currencies: [USD, GBP, JPY, EUR]
        // INFO: SLA for EXPRESS: 60
        // INFO: Currency for AU: AUD
    }
}