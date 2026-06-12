// Java 8+
/**
 * Listing 13.8 — CollectionAggregation.java
 * Demonstrates: Aggregation with Collection APIs — frequency, merge, max, min, disjoint
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 8+
 */
package chapter13;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class CollectionAggregation {

    private static final Logger log = Logger.getLogger(CollectionAggregation.class.getName());

    public static void main(String[] args) {

        List<String> events = List.of(
                "LOGIN", "PURCHASE", "LOGIN",
                "LOGOUT", "PURCHASE", "LOGIN",
                "PURCHASE", "PURCHASE", "LOGOUT");

        // Collections.frequency — count one specific element without a stream
        int loginCount = Collections.frequency(events, "LOGIN");
        log.info("Logins: " + loginCount); // 3

        // merge — build a full frequency map without streams
        Map<String, Integer> frequency = new HashMap<>();
        events.forEach(event ->
                frequency.merge(event, 1, Integer::sum)); // insert 1 or add to existing
        log.info("Frequency map: " + frequency); // LOGIN=3, PURCHASE=4, LOGOUT=2

        // Collections.max and min — no sorting required
        List<Integer> scores = List.of(42, 15, 78, 94, 33, 61);
        int highest = Collections.max(scores); // 94
        int lowest  = Collections.min(scores); // 15
        log.info("Highest score: " + highest);
        log.info("Lowest score:  " + lowest);

        // Collections.disjoint — true if the two collections share no elements
        List<String> teamA = List.of("Alice", "Bob", "Carol");
        List<String> teamB = List.of("Dave", "Eve", "Frank");
        boolean noOverlap = Collections.disjoint(teamA, teamB); // true
        log.info("No overlap between teams: " + noOverlap);

        // Overlapping example — disjoint returns false
        List<String> teamC = List.of("Alice", "Dave", "Grace");
        boolean hasOverlap = Collections.disjoint(teamA, teamC); // false — Alice shared
        log.info("No overlap between teamA and teamC: " + hasOverlap);

        // Output:
        // Logins: 3
        // Frequency map: {LOGIN=3, PURCHASE=4, LOGOUT=2}
        // Highest score: 94
        // Lowest score:  15
        // No overlap between teams: true
        // No overlap between teamA and teamC: false
    }
}