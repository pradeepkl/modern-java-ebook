// Java 25+
// Feature shown: immutable collections shared across threads without locks, final in Java 16+
/**
 * Listing 12.10 — ImmutableCollectionConcurrency.java
 * Demonstrates: immutable collections shared safely across threads without synchronisation
 * Chapter 12: Collections, Ownership, and State Safety
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter12;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ImmutableCollectionConcurrency {

    private static final Logger log =
            Logger.getLogger(ImmutableCollectionConcurrency.class.getName());

    // Immutable record — no mutable fields, safe to share across threads
    record Product(String productId, String name, double price) {}

    void main() throws InterruptedException {

        // Immutable product catalogue — safe to share across threads
        // without any synchronisation; List.of() guarantees immutability
        List<Product> catalogue = List.of(
                new Product("P001", "Laptop", 999.99),
                new Product("P002", "Phone", 599.99),
                new Product("P003", "Tablet", 449.99));

        ExecutorService executor = Executors.newFixedThreadPool(8);

        // All 8 threads read the same immutable list concurrently
        // No locks, no ConcurrentHashMap, no volatile needed
        // Immutability guarantee makes concurrent reads inherently safe
        for (int i = 0; i < 8; i++) {
            executor.submit(() -> {
                double total = catalogue.stream()
                        .mapToDouble(Product::price)
                        .sum();
                log.info("Catalogue total: " + total);
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Output:
        // INFO: Catalogue total: 2049.97
        // INFO: Catalogue total: 2049.97
        // INFO: Catalogue total: 2049.97
        // (repeated 8 times — all threads see the same consistent result)
    }
}