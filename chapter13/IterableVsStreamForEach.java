// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 13.5 — IterableVsStreamForEach.java
 * Demonstrates: Iterable.forEach (ordered) vs stream().forEach (unordered parallel)
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter13;

import java.util.List;
import java.util.logging.Logger;

public class IterableVsStreamForEach {

    private static final Logger log =
            Logger.getLogger(IterableVsStreamForEach.class.getName());

    void main() {

        List<String> items = List.of("A", "B", "C", "D", "E");

        // Iterable.forEach — synchronous, guaranteed encounter order
        log.info("--- Iterable.forEach (encounter order guaranteed) ---");
        items.forEach(item -> log.info("Iterable: " + item)); // A, B, C, D, E always

        // stream().forEach on a sequential stream — ordered in practice
        log.info("--- stream().forEach (sequential) ---");
        items.stream()
             .forEach(item -> log.info("Sequential stream: " + item));

        // parallelStream().forEach — no order guarantee
        log.info("--- parallelStream().forEach (order NOT guaranteed) ---");
        items.parallelStream()
             .forEach(item -> log.info("Parallel stream: " + item)); // any order

        // parallelStream().forEachOrdered — restores order at a cost
        log.info("--- parallelStream().forEachOrdered (order restored) ---");
        items.parallelStream()
             .forEachOrdered(item -> log.info("Ordered parallel: " + item)); // A, B, C, D, E

        // Output:
        // --- Iterable.forEach (encounter order guaranteed) ---
        // Iterable: A
        // Iterable: B
        // Iterable: C
        // Iterable: D
        // Iterable: E
        // --- stream().forEach (sequential) ---
        // Sequential stream: A  ... E  (in order)
        // --- parallelStream().forEach (order NOT guaranteed) ---
        // Parallel stream: C  (any order — varies per run)
        // --- parallelStream().forEachOrdered (order restored) ---
        // Ordered parallel: A  ... E  (in order, slower)
    }
}