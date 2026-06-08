// Java 10+
/**
 * Listing 2.9 — WithVar.java
 * Demonstrates: Local variable type inference using the var keyword
 * Chapter 2: Expressing Intent with Modern Java
 * Requires: Java 10+
 */
package chapter02;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WithVar {

    public static void main(String[] args) {
        // var infers exact same types as explicit declarations — no dynamic typing
        var map = Map.of("A", 1, "B", 2); // inferred as Map<String, Integer>

        var entries = map.entrySet(); // inferred as Set<Map.Entry<String, Integer>>

        var iterator = entries.iterator(); // inferred as Iterator<Map.Entry<String, Integer>>

        while (iterator.hasNext()) {
            var entry = iterator.next(); // inferred as Map.Entry<String, Integer>
            // Type safety is fully preserved — getKey() and getValue() work as expected
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
// Output:
// A: 1
// B: 2