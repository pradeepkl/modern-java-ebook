// Java 25+
// Feature shown: stream flatMap operator (one-to-many flattening), final in Java 8+

/**
 * Listing 14.7 — FlatMapOperator.java
 * Demonstrates: flatMap for one-to-many flattening of nested collections,
 *               and flatMap with Optional to eliminate null values
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;

record OrderLine(String productId, int quantity, double unitPrice) {}

record OrderWithLines(String orderId, List<OrderLine> lines) {}

public class FlatMapOperator {

    private static final Logger LOG = Logger.getLogger(FlatMapOperator.class.getName());

    static List<OrderWithLines> getOrdersWithLines() {
        return List.of(
            new OrderWithLines("ORD-1", List.of(
                new OrderLine("P001", 2, 30.0),
                new OrderLine("P002", 5, 25.0)
            )),
            new OrderWithLines("ORD-2", List.of(
                new OrderLine("P003", 1, 120.0),
                new OrderLine("P004", 3, 10.0)
            )),
            new OrderWithLines("ORD-3", List.of(
                new OrderLine("P005", 10, 15.0)
            ))
        );
    }

    void main() {
        List<OrderWithLines> orders = getOrdersWithLines();

        // map() produces Stream<List<OrderLine>> — a stream of lists
        Stream<List<OrderLine>> nested = orders.stream()
                .map(OrderWithLines::lines); // each element is a List
        LOG.info("Nested stream element count: " + nested.count());

        // flatMap() flattens each order's lines into one stream
        List<OrderLine> allLines = orders.stream()
                .flatMap(order -> order.lines().stream()) // one-to-many
                .toList();
        LOG.info("All lines count: " + allLines.size());

        // Filter individual lines after flattening
        List<OrderLine> highValueLines = orders.stream()
                .flatMap(order -> order.lines().stream())
                .filter(line -> line.quantity() * line.unitPrice() > 100.0)
                .toList();
        LOG.info("High-value lines: " + highValueLines);

        // flatMap with Optional — eliminate null values
        List<String> optionalValues = Arrays.asList("present1", null, "present2", null);
        List<String> nonNull = optionalValues.stream()
                .flatMap(v -> Optional.ofNullable(v).stream()) // absent = empty stream
                .toList();
        LOG.info("Non-null values: " + nonNull);

        // Output:
        // Nested stream element count: 3
        // All lines count: 5
        // High-value lines: [OrderLine[productId=P003, quantity=1, unitPrice=120.0], OrderLine[productId=P005, quantity=10, unitPrice=15.0]]
        // Non-null values: [present1, present2]
    }
}