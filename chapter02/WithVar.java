// Java 25+
// Feature shown: var (local variable type inference), final in Java 10+
package chapter02;

/**
 * Listing 2.2 — WithVar.java
 * Demonstrates: var keyword for local variable type inference
 * Chapter 2: Writing Java the Modern Way
 * Requires: Java 25+ (instance main method via JEP 512)
 */

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class WithVar {

    private static final Logger log =
            Logger.getLogger(WithVar.class.getName());

    void main() {
        // Without var: type declared explicitly on both sides
        Map<String, Integer> explicit = Map.of("A", 1, "B", 2);
        Set<Map.Entry<String, Integer>> explicitEntries = explicit.entrySet();
        log.info("Explicit entry count: " + explicitEntries.size());

        // With var: compiler infers Map<String, Integer> from the initializer
        var map = Map.of("A", 1, "B", 2);       // inferred: Map<String, Integer>
        var entries = map.entrySet();             // inferred: Set<Map.Entry<String, Integer>>
        var iterator = entries.iterator();        // inferred: Iterator<Map.Entry<String, Integer>>

        while (iterator.hasNext()) {
            var entry = iterator.next();          // inferred: Map.Entry<String, Integer>
            log.info(entry.getKey() + ": " + entry.getValue());
        }

        // var is still statically typed — the compiler rejects type mismatches
        var count = 42;                           // inferred: int
        // count = "text";                        // compile error: incompatible types

        // var requires an initializer — cannot be used without one
        // var unknown;                           // compile error: cannot infer type

        log.info("count value: " + count);

        // Output:
        // INFO: Explicit entry count: 2
        // INFO: A: 1
        // INFO: B: 2
        // INFO: count value: 42
    }
}