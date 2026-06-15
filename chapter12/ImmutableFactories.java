// Java 25+
// Feature shown: List.of / Set.of / Map.of immutable factories, final in Java 9+
/**
 * Listing 12.6 — ImmutableFactories.java
 * Demonstrates: List.of, Set.of, Map.of, and Map.ofEntries immutable collection factories
 * Chapter 12: Collections, Ownership, and State Safety
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter12;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ImmutableFactories {

    private static final Logger log =
            Logger.getLogger(ImmutableFactories.class.getName());

    void main() {

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
            statuses.add("CANCELLED");
        } catch (UnsupportedOperationException e) {
            log.info("Cannot modify — structurally immutable");
        }

        // Null elements are rejected at construction time
        try {
            List<String> withNull =
                    List.of("PENDING", null, "SHIPPED");
        } catch (NullPointerException e) {
            log.info("Null rejected — use Optional for absent values");
        }

        log.info("Statuses: " + statuses);
        log.info("Valid currencies: " + validCurrencies);
        log.info("SLA for EXPRESS: " + slaMinutes.get("EXPRESS"));
        log.info("Region codes size: " + regionCodes.size());

        // Output:
        // INFO: Cannot modify — structurally immutable
        // INFO: Null rejected — use Optional for absent values
        // INFO: Statuses: [PENDING, CONFIRMED, SHIPPED, DELIVERED]
        // INFO: Valid currencies: [USD, GBP, EUR, JPY]
        // INFO: SLA for EXPRESS: 60
        // INFO: Region codes size: 5
    }
}