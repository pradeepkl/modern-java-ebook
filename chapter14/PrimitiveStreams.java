// Java 25+
// Feature shown: primitive streams (IntStream, LongStream, DoubleStream), final in Java 8+

/**
 * Listing 14.17 — PrimitiveStreams.java
 * Demonstrates: primitive stream types, aggregation operations, summaryStatistics,
 *               boxing/unboxing boundaries, and primitive Optional variants
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.logging.Logger;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class PrimitiveStreams {

    private static final Logger log = Logger.getLogger(PrimitiveStreams.class.getName());

    record Order(String orderId, int quantity, double amount) {}

    private static List<Order> getOrders() {
        return List.of(
            new Order("ORD-1", 3, 29.99),
            new Order("ORD-2", 1, 9.99),
            new Order("ORD-3", 5, 49.95),
            new Order("ORD-4", 2, 19.50)
        );
    }

    void main() {
        List<Order> orders = getOrders();

        // Stream<Integer> path — boxes every element
        int totalBoxed = orders.stream()
                .map(Order::quantity)
                .mapToInt(Integer::intValue)   // unbox to IntStream
                .sum();
        log.info("Total (boxed path): " + totalBoxed);

        // Direct primitive stream — no boxing
        double total = orders.stream()
                .mapToDouble(Order::amount)
                .sum();
        log.info("Total amount: " + total);

        double average = orders.stream()
                .mapToDouble(Order::amount)
                .average()
                .orElse(0.0);                  // returns OptionalDouble
        log.info("Average amount: " + average);

        int maxQuantity = orders.stream()
                .mapToInt(Order::quantity)
                .max()
                .orElse(0);                    // returns OptionalInt
        log.info("Max quantity: " + maxQuantity);

        // summaryStatistics — five aggregations in one pass
        DoubleSummaryStatistics stats = orders.stream()
                .mapToDouble(Order::amount)
                .summaryStatistics();
        log.info("Count: " + stats.getCount());
        log.info("Sum:   " + stats.getSum());
        log.info("Min:   " + stats.getMin());
        log.info("Max:   " + stats.getMax());
        log.info("Avg:   " + stats.getAverage());

        // boxed() — IntStream back to Stream<Integer>
        List<Integer> boxedList = IntStream.rangeClosed(1, 5)
                .boxed()
                .toList();
        log.info("Boxed list: " + boxedList);

        // mapToObj() — int to String, no boxing of int itself
        List<String> labels = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> "Order-" + i)
                .toList();
        log.info("Labels: " + labels);

        // Primitive Optional variants — no boxing
        OptionalInt    maxQty = orders.stream().mapToInt(Order::quantity).max();
        OptionalDouble avgAmt = orders.stream().mapToDouble(Order::amount).average();
        OptionalLong   minId  = LongStream.of(1L, 2L, 3L).min();

        maxQty.ifPresent(qty -> log.info("Max quantity: " + qty));
        int safeMax = maxQty.orElse(0);        // returns int, not Integer
        log.info("Safe max: " + safeMax);
        avgAmt.ifPresent(a -> log.info("Avg amount: " + a));
        minId.ifPresent(id -> log.info("Min id: " + id));

        // Output:
        // Total (boxed path): 11
        // Total amount: 109.43
        // Average amount: 27.3575
        // Max quantity: 5
        // Count: 4
        // Sum:   109.43
        // Min:   9.99
        // Max:   49.95
        // Avg:   27.3575
        // Boxed list: [1, 2, 3, 4, 5]
        // Labels: [Order-1, Order-2, Order-3, Order-4, Order-5]
        // Max quantity: 5
        // Safe max: 5
        // Avg amount: 27.3575
        // Min id: 1
    }
}