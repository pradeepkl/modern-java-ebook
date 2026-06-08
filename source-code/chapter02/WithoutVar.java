// Java 9+
/**
 * Listing 2.8 — WithoutVar.java
 * Demonstrates: Verbose explicit type declarations before var keyword (Java 9 and earlier style)
 * Chapter 2: Writing Code the Modern Java Way
 * Requires: Java 9+
 */
package chapter02;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WithoutVar {

    public static void main(String[] args) {
        // All types explicitly repeated, even when obvious from context
        Map<String, Integer> map = Map.of("A", 1, "B", 2);

        // Verbose: full generic type repeated for Set
        Set<Map.Entry<String, Integer>> entries = map.entrySet();

        // Verbose: full generic type repeated for Iterator
        Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();

        while (iterator.hasNext()) {
            // Verbose: full generic type repeated for each entry
            Map.Entry<String, Integer> entry = iterator.next();
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // Output:
    // A: 1
    // B: 2
}