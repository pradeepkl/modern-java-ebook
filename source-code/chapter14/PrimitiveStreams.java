// Java 25+
// Feature shown: primitive streams (IntStream, LongStream, DoubleStream), final in Java 8+

/**
 * Listing 14.17 — PrimitiveStreams.java
 * Demonstrates: primitive stream types, aggregation, summaryStatistics,
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

    static List<Order> getOrders() {
        return List.of(
            new Order("ORD-1", 3, 29.99),
            new Order("ORD-2", 1, 9.99),
            new Order("ORD-3", 5, 49.95),
            new Order("ORD-4", 2, 19.50)
        );
    }

    void main() {
        List<Order> orders = getOrders();

        // Stream<Integer> path — each element is a boxed Integer object
        List<Integer> quantities = orders.stream()
                .map(Order::quantity)
                .toList();
        int totalBoxed = quantities.stream()
                .mapToInt(Integer::intValue) // unbox to IntStream
                .sum();
        log.info("Total (boxed path): " + totalBoxed);

        // IntStream directly — no boxing at all
        int totalPrimitive = orders.stream()
                .mapToInt(Order::quantity)   // Stream<Order> -> IntStream
                .sum();
        log.info("Total (primitive path): " + totalPrimitive);

        // LongStream from orderId strings
        LongStream orderIds = orders.stream()
                .mapToLong(o -> Long.parseLong(o.orderId().replace("ORD-", "")));
        log.info("Order id sum: " + orderIds.sum());

        // DoubleStream aggregations
        double total   = orders.stream().mapToDouble(Order::amount).sum();
        double average = orders.stream().mapToDouble(Order::amount).average().orElse(0.0);
        int maxQty     = orders.stream().mapToInt(Order::quantity).max().orElse(0);
        log.info("Total amount: " + total);
        log.info("Average amount: " + average);
        log.info("Max quantity: " + maxQty);

        // summaryStatistics — five aggregations in one pass
        DoubleSummaryStatistics stats = orders.stream()
                .mapToDouble(Order::amount)
                .summaryStatistics();
        log.info("Count: " + stats.getCount());
        log.info("Sum:   " + stats.getSum());
        log.info("Min:   " + stats.getMin());
        log.info("Max:   " + stats.getMax());
        log.info("Avg:   " + stats.getAverage());

        // boxed() — IntStream -> Stream<Integer>
        List<Integer> boxedList = IntStream.rangeClosed(1, 5).boxed().toList();
        log.info("Boxed list: " + boxedList);

        // mapToObj() — int -> String, no boxing of int itself
        List<String> labels = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> "Order-" + i)
                .toList();
        log.info("Labels: " + labels);

        // Primitive Optional variants — no boxing
        OptionalInt    maxQtyOpt = orders.stream().mapToInt(Order::quantity).max();
        OptionalDouble avgAmt    = orders.stream().mapToDouble(Order::amount).average();
        OptionalLong   minId     = LongStream.of(1L, 2L, 3L).min();

        maxQtyOpt.ifPresent(qty -> log.info("Max quantity: " + qty));
        int safeMax = maxQtyOpt.orElse(0); // returns int, not Integer
        log.info("Safe max: " + safeMax);
        log.info("Avg amount present: " + avgAmt.isPresent());
        log.info("Min id: " + minId.orElse(-1L));

        // Output:
        // Total (boxed path): 11
        // Total (primitive path): 11
        // Order id sum: 10
        // Total amount: 109.43
        // Average amount: 27.3575
        // Max quantity: 5
        // Count: 4  Sum: 109.43  Min: 9.99  Max: 49.95  Avg: 27.3575
        // Boxed list: [1, 2, 3, 4, 5]
        // Labels: [Order-1, Order-2, Order-3, Order-4, Order-5]
        // Max quantity: 5
        // Safe max: 5
        // Avg amount present: true
        // Min id: 1
    }
}