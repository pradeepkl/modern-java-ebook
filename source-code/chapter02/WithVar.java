// Java 25+
// Feature shown: var (local variable type inference), final in Java 10+

/**
 * Listing 2.2 — WithVar.java
 * Demonstrates: var keyword for local variable type inference
 * Chapter 2: Writing Java the Modern Way
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter02;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class WithVar {

    private static final Logger log =
            Logger.getLogger(WithVar.class.getName());

    void main() {
        // Without var: type declared explicitly on both sides of the assignment
        Map<String, Integer> explicit = Map.of("A", 1, "B", 2);
        Set<Map.Entry<String, Integer>> explicitEntries = explicit.entrySet();
        Iterator<Map.Entry<String, Integer>> explicitIterator =
                explicitEntries.iterator();
        log.info("Explicit type declarations: " + explicitIterator.hasNext());

        // With var: compiler infers Map<String, Integer> from the initializer
        var map = Map.of("A", 1, "B", 2);       // inferred: Map<String, Integer>
        var entries = map.entrySet();             // inferred: Set<Map.Entry<...>>
        var iterator = entries.iterator();        // inferred: Iterator<Map.Entry<...>>

        while (iterator.hasNext()) {
            var entry = iterator.next();          // inferred: Map.Entry<String, Integer>
            log.info(entry.getKey() + ": " + entry.getValue());
        }

        // var is still statically typed — the compiler rejects type mismatches
        // var name = 42;
        // name = "text";  // compile error: incompatible types

        // var requires an initializer — the compiler must have a type to infer
        // var unknown;    // compile error: cannot infer type
    }

    // Output:
    // INFO: Explicit type declarations: true
    // INFO: A: 1
    // INFO: B: 2
}