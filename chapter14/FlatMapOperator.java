// Java 25+
// Feature shown: stream flatMap operator (one-to-many flattening), final in Java 8+

/**
 * Listing 14.7 — FlatMapOperator.java
 * Demonstrates: flatMap for one-to-many flattening, nested stream vs flat stream,
 *               filtering flattened lines, and flatMap with Optional for null elimination
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

    private static List<OrderWithLines> getOrdersWithLines() {
        return List.of(
            new OrderWithLines("ORD-1", List.of(
                new OrderLine("P001", 3, 20.0),   // 60.0 — below threshold
                new OrderLine("P002", 5, 30.0)    // 150.0 — high value
            )),
            new OrderWithLines("ORD-2", List.of(
                new OrderLine("P003", 2, 60.0),   // 120.0 — high value
                new OrderLine("P004", 1, 10.0)    // 10.0 — below threshold
            ))
        );
    }

    void main() {
        List<OrderWithLines> orders = getOrdersWithLines();

        // map() produces Stream<List<OrderLine>> — a stream of lists, not order lines
        Stream<List<OrderLine>> nested = orders.stream()
                .map(OrderWithLines::lines);
        LOG.info("Nested stream count (lists): " + nested.count()); // 2 lists

        // flatMap() flattens each order's list into one stream of OrderLine
        // SQL equivalent: UNNEST / JOIN with order lines
        List<OrderLine> allLines = orders.stream()
                .flatMap(order -> order.lines().stream())
                .toList();
        LOG.info("All lines (flattened): " + allLines.size()); // 4 lines

        // Filter individual lines after flattening
        // Business: find all high-value order lines (quantity * unitPrice > 100)
        List<OrderLine> highValueLines = orders.stream()
                .flatMap(order -> order.lines().stream())
                .filter(line -> line.quantity() * line.unitPrice() > 100.0)
                .toList();
        LOG.info("High-value lines: " + highValueLines); // P002, P003

        // flatMap with Optional — flatten absent values, eliminate nulls
        // SQL equivalent: LEFT JOIN with null elimination
        // Arrays.asList used here — List.of() rejects null elements
        List<String> optionalValues = Arrays.asList("present1", null, "present2", null);
        List<String> nonNull = optionalValues.stream()
                .flatMap(v -> Optional.ofNullable(v).stream())
                .toList();
        LOG.info("Non-null values: " + nonNull); // [present1, present2]

        // Output:
        // Nested stream count (lists): 2
        // All lines (flattened): 4
        // High-value lines: [OrderLine[productId=P002, ...], OrderLine[productId=P003, ...]]
        // Non-null values: [present1, present2]
    }
}