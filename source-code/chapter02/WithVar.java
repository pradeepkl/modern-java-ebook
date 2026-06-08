// Java 10+
/**
 * Listing 2.9 — WithVar.java
 * Demonstrates: Using var keyword for local type inference
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
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // var is still statically typed — calling wrong methods would fail at compile time
        var greeting = "Hello, var!"; // inferred as String
        System.out.println(greeting.toUpperCase()); // String methods available
    }

    // Output:
    // A: 1
    // B: 2
    // HELLO, VAR!
}