// Java 16+
/**
 * Listing 10.10 — ImmutableCollectionConcurrency.java
 * Demonstrates: Immutable collections shared safely across threads without synchronisation
 * Chapter 10: Collections, Ownership, and State Safety
 * Requires: Java 16+
 */
package chapter10;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ImmutableCollectionConcurrency {

    private static final Logger log =
            Logger.getLogger(ImmutableCollectionConcurrency.class.getName());

    // Immutable record — no setters, all fields final
    record Product(String productId, String name, double price) {}

    public static void main(String[] args) throws InterruptedException {

        // Immutable product catalogue — safe to share
        // across threads without any synchronisation
        List<Product> catalogue = List.of(
                new Product("P001", "Laptop", 999.99),
                new Product("P002", "Phone", 599.99),
                new Product("P003", "Tablet", 449.99));

        ExecutorService executor = Executors.newFixedThreadPool(8);

        // All 8 threads read the same immutable list
        // No locks, no ConcurrentHashMap, no volatile
        // The immutability guarantee makes it safe
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                double total = catalogue.stream()
                        .mapToDouble(Product::price)
                        .sum(); // No race condition — list never changes
                log.info("Catalogue total: " + total);
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Confirm the catalogue is unchanged after concurrent reads
        log.info("Catalogue size after concurrent access: " + catalogue.size());

        // Output:
        // INFO: Catalogue total: 2049.97
        // INFO: Catalogue total: 2049.97
        // ... (repeated 10 times, always consistent)
        // INFO: Catalogue size after concurrent access: 3
    }
}