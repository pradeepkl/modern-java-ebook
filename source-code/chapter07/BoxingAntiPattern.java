// Java 8+
/**
 * Listing 7.2 — BoxingAntiPattern.java
 * Demonstrates: Unnecessary boxing/unboxing transitions in stream pipelines
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 8+
 */
package chapter07;

import java.util.List;
import java.util.logging.Logger;

public class BoxingAntiPattern {

    private static final Logger log =
            Logger.getLogger(BoxingAntiPattern.class.getName());

    // Simple order record with primitive fields
    record Order(String id, int quantity, double unitPrice) {}

    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("P100", 2,  499.99),
                new Order("P200", 5,   99.50),
                new Order("P300", 1, 1299.00));

        // ❌ Anti-pattern: unnecessary object ↔ primitive transitions
        // Step 1: map() produces Stream<Double> — result is autoboxed
        // Step 2: mapToDouble() unboxes each Double back to primitive
        // Step 3: boxed() re-boxes each double into Double again
        // Step 4: mapToDouble() unboxes once more — completely wasteful
        double totalRevenue = orders.stream()
                .map(order ->
                        order.quantity()
                        * order.unitPrice()) // autoboxed to Double
                .mapToDouble(Double::doubleValue) // unbox to primitive
                .boxed()                          // box again to Double
                .mapToDouble(Double::doubleValue) // unbox again — wasteful
                .sum();

        log.info("Total revenue (anti-pattern): " + totalRevenue);

        // ✅ Preferred: cross into primitive world exactly once
        double efficientTotal = orders.stream()
                .mapToDouble(order -> order.quantity() * order.unitPrice())
                .sum();

        log.info("Total revenue (efficient):    " + efficientTotal);

        log.info("Results match: " + (totalRevenue == efficientTotal));

        // Output:
        // INFO: Total revenue (anti-pattern): 2597.48
        // INFO: Total revenue (efficient):    2597.48
        // INFO: Results match: true
    }
}