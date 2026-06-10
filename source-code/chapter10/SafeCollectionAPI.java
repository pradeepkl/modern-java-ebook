// Java 16+
/**
 * Listing 10.11 — SafeCollectionAPI.java
 * Demonstrates: Generic repository with safe, immutable collection APIs
 * Chapter 10: Collections, Ownership, and State Safety
 * Requires: Java 16+
 */
package chapter10;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SafeCollectionAPI {

    private static final Logger log =
            Logger.getLogger(SafeCollectionAPI.class.getName());

    record Order(String orderId, double amount, String status, String customerId) {}

    // Generic repository — safe collection API for any entity type T
    static class Repository<T> {

        private final List<T> store = new ArrayList<>();

        public void save(T item) {
            store.add(item); // Mutates internal state
        }

        // Returns an immutable snapshot — callers cannot affect repository state
        public List<T> findAll() {
            return List.copyOf(store); // Defensive copy
        }

        // Returns an immutable filtered snapshot; caller supplies predicate
        public List<T> findBy(Predicate<T> pred) {
            return store.stream()
                    .filter(pred)
                    .toList(); // Java 16+ — unmodifiable result
        }

        // Returns an immutable grouped result; caller supplies key extractor
        public <K> Map<K, List<T>> groupBy(Function<T, K> keyExtractor) {
            return store.stream()
                    .collect(Collectors.groupingBy(
                            keyExtractor,
                            Collectors.toUnmodifiableList())); // Immutable values
        }
    }

    public static void main(String[] args) {
        Repository<Order> repo = new Repository<>();
        repo.save(new Order("ORD-001", 99.99, "CONFIRMED", "C1"));
        repo.save(new Order("ORD-002", 49.99, "PENDING", "C2"));
        repo.save(new Order("ORD-003", 149.99, "CONFIRMED", "C1"));

        // Generic filter — predicate supplied by caller
        List<Order> confirmed = repo.findBy(o -> o.status().equals("CONFIRMED"));
        log.info("Confirmed: " + confirmed.size()); // 2

        // Generic grouping — key extractor supplied by caller
        Map<String, List<Order>> byCustomer = repo.groupBy(Order::customerId);
        log.info("C1 orders: " + byCustomer.get("C1").size()); // 2

        // Same repository works for any type
        Repository<String> tags = new Repository<>();
        tags.save("performance");
        tags.save("security");
        tags.save("performance");

        List<String> perfTags = tags.findBy(t -> t.equals("performance"));
        log.info("Performance tags: " + perfTags.size()); // 2

        // Output:
        // INFO: Confirmed: 2
        // INFO: C1 orders: 2
        // INFO: Performance tags: 2
    }
}