// Java 25+
// Feature shown: collection aggregation utilities (Collections.frequency, merge, max, min, disjoint), final in Java 8+

/**
 * Listing 13.8 — CollectionAggregation.java
 * Demonstrates: Aggregation with Collection APIs — frequency counting,
 * merge-based frequency maps, max/min, and disjoint checks
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter13;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class CollectionAggregation {

    private static final Logger log = Logger.getLogger(CollectionAggregation.class.getName());

    void main() {
        List<String> events = List.of(
                "LOGIN", "PURCHASE", "LOGIN",
                "LOGOUT", "PURCHASE", "LOGIN",
                "PURCHASE", "PURCHASE", "LOGOUT");

        // Collections.frequency — count one specific element without a stream
        int loginCount = Collections.frequency(events, "LOGIN");
        log.info("Logins: " + loginCount); // 3

        // merge — build a full frequency map without streams
        // If key absent: store 1; if present: add 1 to existing count
        Map<String, Integer> frequency = new HashMap<>();
        events.forEach(event ->
                frequency.merge(event, 1, Integer::sum));
        log.info("LOGIN count: "    + frequency.get("LOGIN"));    // 3
        log.info("PURCHASE count: " + frequency.get("PURCHASE")); // 4
        log.info("LOGOUT count: "   + frequency.get("LOGOUT"));   // 2

        // Collections.max and min — find extremes in a list
        List<Integer> scores = List.of(42, 15, 78, 94, 33, 61);
        int highest = Collections.max(scores); // 94
        int lowest  = Collections.min(scores); // 15
        log.info("Highest score: " + highest);
        log.info("Lowest score: "  + lowest);

        // Collections.disjoint — true if the two collections share no elements
        List<String> teamA = List.of("Alice", "Bob", "Carol");
        List<String> teamB = List.of("Dave", "Eve", "Frank");
        boolean noOverlap = Collections.disjoint(teamA, teamB); // true
        log.info("No overlap between teams: " + noOverlap);

        List<String> teamC = List.of("Carol", "Dave", "Eve");
        boolean hasOverlap = Collections.disjoint(teamA, teamC); // false
        log.info("No overlap between teamA and teamC: " + hasOverlap);

        // Output:
        // Logins: 3
        // LOGIN count: 3
        // PURCHASE count: 4
        // LOGOUT count: 2
        // Highest score: 94
        // Lowest score: 15
        // No overlap between teams: true
        // No overlap between teamA and teamC: false
    }
}