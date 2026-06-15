// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 13.15 — ComputeIfAbsentChaining.java
 * Demonstrates: correct usage of computeIfAbsent chaining to add to a list value
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ComputeIfAbsentChaining {

    private static final Logger LOG =
            Logger.getLogger(ComputeIfAbsentChaining.class.getName());

    void main() {
        Map<String, List<String>> map = new HashMap<>();

        // WRONG approach: computeIfAbsent returns the value (the list),
        // not the map — storing the result in a variable named "map2"
        // and calling add on it is fine, but assigning to the map is wrong.
        // This illustrates the mistake: treating the return as the map.
        Map<String, List<String>> wrongResult =
                (Map<String, List<String>>) (Object)
                map.computeIfAbsent("orders", k -> new ArrayList<>());
        // wrongResult is actually a List, not a Map — this would fail at runtime.
        // The compiler cannot catch this without generics; shown here conceptually.

        // Correct approach: chain directly onto the returned list
        map.computeIfAbsent("orders", k -> new ArrayList<>())
           .add("order-001"); // adds to the list stored under "orders"

        map.computeIfAbsent("orders", k -> new ArrayList<>())
           .add("order-002"); // key already exists; existing list is returned

        map.computeIfAbsent("returns", k -> new ArrayList<>())
           .add("return-101"); // new key; new list created and item added

        LOG.info("orders  => " + map.get("orders"));
        LOG.info("returns => " + map.get("returns"));

        // Demonstrate that the return value is the list, not the map
        List<String> orderList =
                map.computeIfAbsent("orders", k -> new ArrayList<>());
        orderList.add("order-003"); // same list, mutated in place
        LOG.info("orders after extra add => " + map.get("orders"));

        // Output:
        // orders  => [order-001, order-002]
        // returns => [return-101]
        // orders after extra add => [order-001, order-002, order-003]
    }
}