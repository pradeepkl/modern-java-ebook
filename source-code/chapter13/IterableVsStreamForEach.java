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
        log.info("=== Iterable.forEach (encounter order guaranteed) ===");
        items.forEach(item -> log.info("Iterable: " + item)); // A, B, C, D, E always

        // stream().forEach on a sequential stream — ordered in practice
        log.info("=== stream().forEach (sequential, ordered in practice) ===");
        items.stream()
             .forEach(item -> log.info("Sequential stream: " + item));

        // parallelStream().forEach — no order guarantee
        log.info("=== parallelStream().forEach (order NOT guaranteed) ===");
        items.parallelStream()
             .forEach(item -> log.info("Parallel stream: " + item)); // any order

        // parallelStream().forEachOrdered — restores order at a performance cost
        log.info("=== parallelStream().forEachOrdered (order restored) ===");
        items.parallelStream()
             .forEachOrdered(item -> log.info("Ordered parallel: " + item)); // A, B, C, D, E

        // Output:
        // INFO: === Iterable.forEach (encounter order guaranteed) ===
        // INFO: Iterable: A
        // INFO: Iterable: B
        // INFO: Iterable: C
        // INFO: Iterable: D
        // INFO: Iterable: E
        // INFO: === stream().forEach (sequential, ordered in practice) ===
        // INFO: Sequential stream: A  ... E
        // INFO: === parallelStream().forEach (order NOT guaranteed) ===
        // INFO: Parallel stream: C  (any order — varies per run)
        // INFO: === parallelStream().forEachOrdered (order restored) ===
        // INFO: Ordered parallel: A  ... E
    }
}