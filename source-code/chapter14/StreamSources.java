// Java 25+
// Feature shown: stream sources (collections, arrays, files, strings, infinite), final in Java 21+

/**
 * Listing 14.2 — StreamSources.java
 * Demonstrates: Various stream source types: collections, arrays, explicit values,
 *               numeric ranges, string sources, and infinite sources.
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (void main() via JEP 512: Compact Source Files and Instance Main Methods)
 */
package chapter14;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamSources {

    private static final Logger LOG = Logger.getLogger(StreamSources.class.getName());

    record Order(String id, double amount) {}

    static List<Order> getOrders() {
        return List.of(
            new Order("O1", 120.0), new Order("O2", 80.0),
            new Order("O3", 200.0), new Order("O4", 55.0),
            new Order("O5", 310.0), new Order("O6", 99.0)
        );
    }

    static Map<String, Double> getPrices() {
        return Map.of("Widget", 9.99, "Gadget", 24.99, "Doohickey", 4.49);
    }

    static String getStatus() { return "CONFIRMED"; }

    void main() {
        List<Order> orders = getOrders();

        // Collection sources
        long count = orders.stream().filter(o -> o.amount() > 100.0).count();
        LOG.info("Orders over 100: " + count);

        // Set source — no guaranteed encounter order
        Set<String> regions = Set.of("UK", "US", "EU");
        long regionCount = regions.stream().count();
        LOG.info("Region count: " + regionCount);

        // Map entry source
        double totalPrice = getPrices().entrySet().stream()
                .mapToDouble(Map.Entry::getValue).sum();
        LOG.info("Total price sum: " + totalPrice);

        // Array source — partial slice (indices 0 to 4 inclusive)
        Order[] orderArray = orders.toArray(Order[]::new);
        long partialCount = Arrays.stream(orderArray, 0, 5).count();
        LOG.info("Partial array count: " + partialCount);

        // Explicit values and null-safe single-element source
        long statusCount = Stream.of("CONFIRMED", "PENDING", "SHIPPED").count();
        LOG.info("Status count: " + statusCount);
        long nullSafeCount = Stream.ofNullable(getStatus()).count();
        LOG.info("Null-safe count: " + nullSafeCount);

        // Numeric range sources
        long indexCount = IntStream.range(0, orders.size()).count();
        LOG.info("Index count: " + indexCount);
        long pageCount = IntStream.rangeClosed(1, 10).count();
        LOG.info("Page count: " + pageCount);

        // String chars source — uppercase letters in "CONFIRMED"
        long upperCount = "CONFIRMED".chars().filter(Character::isUpperCase).count();
        LOG.info("Uppercase chars in CONFIRMED: " + upperCount);

        // String lines source
        long lineCount = "order1\norder2\norder3".lines().count();
        LOG.info("Line count: " + lineCount);

        // Infinite source with limit — even numbers
        List<Integer> evens = Stream.iterate(0, n -> n + 2).limit(10).toList();
        LOG.info("First 10 evens: " + evens);

        // Iterate with predicate (Java 9+) — no limit needed
        List<Integer> under100 = Stream.iterate(0, n -> n < 100, n -> n + 2).toList();
        LOG.info("Evens under 100 count: " + under100.size());

        // Generate source — random doubles
        long randomCount = Stream.generate(Math::random).limit(5).count();
        LOG.info("Random values generated: " + randomCount);

        // Output:
        // Orders over 100: 3
        // Region count: 3
        // Total price sum: 39.47
        // Partial array count: 5
        // Status count: 3
        // Null-safe count: 1
        // Index count: 6
        // Page count: 10
        // Uppercase chars in CONFIRMED: 9
        // Line count: 3
        // First 10 evens: [0, 2, 4, 6, 8, 10, 12, 14, 16, 18]
        // Evens under 100 count: 50
        // Random values generated: 5
    }
}