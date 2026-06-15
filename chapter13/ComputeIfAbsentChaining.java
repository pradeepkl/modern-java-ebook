// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

package chapter13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Listing 13.15 — ComputeIfAbsentChaining.java
 * Demonstrates: computeIfAbsent returns the value (not the map), so chain add() directly
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
public class ComputeIfAbsentChaining {

    private static final Logger LOG = Logger.getLogger(ComputeIfAbsentChaining.class.getName());

    void main() {
        Map<String, List<String>> map = new HashMap<>();

        // WRONG approach: computeIfAbsent returns the List, not the Map.
        // Assigning to a Map variable causes a compile error or ClassCastException.
        // Map<String, List<String>> wrong = map.computeIfAbsent("fruits", k -> new ArrayList<>());
        // wrong.add("apple"); // compile error — Map has no add()

        // Correct approach: chain add() directly onto the returned List
        map.computeIfAbsent("fruits", k -> new ArrayList<>())
           .add("apple");                                      // adds to the list for "fruits"

        map.computeIfAbsent("fruits", k -> new ArrayList<>())
           .add("banana");                                     // key exists, reuses existing list

        map.computeIfAbsent("vegetables", k -> new ArrayList<>())
           .add("carrot");                                     // creates new list for "vegetables"

        // Log the resulting map contents
        LOG.info("fruits    -> " + map.get("fruits"));
        LOG.info("vegetables -> " + map.get("vegetables"));

        // Demonstrate that the same key is not re-initialised on second call
        int fruitCount = map.get("fruits").size();
        LOG.info("Number of fruits: " + fruitCount);

        // Output:
        // fruits    -> [apple, banana]
        // vegetables -> [carrot]
        // Number of fruits: 2
    }
}