// Java 9+
/**
 * Listing 2.8 — WithoutVar.java
 * Demonstrates: Verbose type declarations without var keyword (Java 9 and earlier style)
 * Chapter 2: Expressing Intent with Modern Java
 * Requires: Java 9+
 */
package chapter02;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WithoutVar {

    public static void main(String[] args) {
        // All types explicitly repeated even when obvious from context
        Map<String, Integer> map = Map.of("A", 1, "B", 2);

        // Type fully repeated despite being obvious from map.entrySet()
        Set<Map.Entry<String, Integer>> entries = map.entrySet();

        // Type fully repeated despite being obvious from entries.iterator()
        Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();

        while (iterator.hasNext()) {
            // Verbose but explicit — type repeated a third time
            Map.Entry<String, Integer> entry = iterator.next();
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // Output:
    // A: 1
    // B: 2
}