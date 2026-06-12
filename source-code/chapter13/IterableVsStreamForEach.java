// Java 8+
/**
 * Listing 13.5 — IterableVsStreamForEach.java
 * Demonstrates: Difference between Iterable.forEach (ordered) and
 *               stream().forEach on parallel streams (unordered)
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 8+
 */
package chapter13;

import java.util.List;
import java.util.logging.Logger;

public class IterableVsStreamForEach {

    private static final Logger log =
            Logger.getLogger(IterableVsStreamForEach.class.getName());

    public static void main(String[] args) {

        List<String> items = List.of("A", "B", "C", "D", "E");

        // Iterable.forEach — synchronous, guaranteed encounter order
        log.info("=== Iterable.forEach (guaranteed order) ===");
        items.forEach(item -> log.info("Iterable: " + item)); // A, B, C, D, E always

        // stream().forEach — sequential stream, order preserved in practice
        log.info("=== stream().forEach (sequential, ordered) ===");
        items.stream()
             .forEach(item -> log.info("Sequential stream: " + item));

        // parallelStream().forEach — no order guarantee; threads interleave
        log.info("=== parallelStream().forEach (parallel, any order) ===");
        items.parallelStream()
             .forEach(item -> log.info("Parallel stream: " + item)); // any order

        // parallelStream().forEachOrdered — restores encounter order at a cost
        log.info("=== parallelStream().forEachOrdered (parallel, ordered) ===");
        items.parallelStream()
             .forEachOrdered(item -> log.info("Ordered parallel: " + item)); // A, B, C, D, E

        // Output:
        // Iterable.forEach prints A B C D E in order, always.
        // parallelStream().forEach may print in any order (e.g. C A E B D).
        // parallelStream().forEachOrdered prints A B C D E but with sync overhead.
    }
}