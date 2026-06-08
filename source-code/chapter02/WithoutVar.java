// Java 9
/**
 * Listing 2.8 — WithoutVar.java
 * Demonstrates: Verbose explicit type declarations in Java 9 and earlier
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

        // Type must be fully spelled out despite being obvious from map.entrySet()
        Set<Map.Entry<String, Integer>> entries = map.entrySet();

        // Iterator type is verbose but required without var
        Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();

        while (iterator.hasNext()) {
            // Entry type repeated again — compiler already knows this type
            Map.Entry<String, Integer> entry = iterator.next(); // verbose but explicit
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // Output:
    // A: 1
    // B: 2
}