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

        // Collections.frequency — count one specific element
        int loginCount = Collections.frequency(events, "LOGIN");
        log.info("Logins: " + loginCount); // 3

        // merge — build a full frequency map without streams
        Map<String, Integer> frequency = new HashMap<>();
        events.forEach(event ->
                frequency.merge(event, 1, Integer::sum));
        // LOGIN=3, PURCHASE=4, LOGOUT=2
        log.info("Frequency map: " + frequency);

        // Collections.max and min on a list of scores
        List<Integer> scores = List.of(42, 15, 78, 94, 61, 33);
        int highest = Collections.max(scores); // 94
        int lowest  = Collections.min(scores); // 15
        log.info("Highest score: " + highest);
        log.info("Lowest score: " + lowest);

        // Collections.disjoint — no shared elements?
        List<String> teamA = List.of("Alice", "Bob", "Carol");
        List<String> teamB = List.of("Dave", "Eve", "Frank");
        boolean noOverlap = Collections.disjoint(teamA, teamB); // true
        log.info("No overlap between teams: " + noOverlap);

        List<String> teamC = List.of("Carol", "Dave", "Eve");
        boolean hasOverlap = Collections.disjoint(teamA, teamC); // false
        log.info("No overlap between teamA and teamC: " + hasOverlap);

        // Output:
        // Logins: 3
        // Frequency map: {LOGIN=3, PURCHASE=4, LOGOUT=2}
        // Highest score: 94
        // Lowest score: 15
        // No overlap between teams: true
        // No overlap between teamA and teamC: false
    }
}