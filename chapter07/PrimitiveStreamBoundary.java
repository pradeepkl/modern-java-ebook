// Java 25+
// Feature shown: primitive stream boundary crossing with mapToDouble, final in Java 8+
/**
 * Listing 7.1 — PrimitiveStreamBoundary.java
 * Demonstrates: crossing from object stream into primitive DoubleStream once
 *               via mapToDouble(), then computing sum() without boxing overhead
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter07;

import java.util.List;
import java.util.logging.Logger;

public class PrimitiveStreamBoundary {

    private static final Logger log =
            Logger.getLogger(PrimitiveStreamBoundary.class.getName());

    // Record holds order data in the object world
    record Order(String id, int quantity, double unitPrice) {}

    void main() {
        // Object collection — reference world for storage and interoperability
        List<Order> orders = List.of(
                new Order("P100", 2,  499.99),  // 999.98
                new Order("P200", 5,   99.50),  // 497.50
                new Order("P300", 1, 1299.00)); // 1299.00

        // Stream<Order>  -> object world (one reference per element)
        // mapToDouble()  -> crosses into primitive DoubleStream exactly once
        // sum()          -> stays in primitive world; no boxing occurs here
        double totalRevenue = orders.stream()
                .mapToDouble(order -> order.quantity() * order.unitPrice())
                .sum();

        // Single boundary crossing: no Double objects created during summation
        log.info("Total revenue: " + totalRevenue);

        // Demonstrate average() — also stays primitive throughout
        double averageOrderValue = orders.stream()
                .mapToDouble(order -> order.quantity() * order.unitPrice())
                .average()
                .orElse(0.0); // OptionalDouble unwrapped to primitive double

        log.info("Average order value: " + averageOrderValue);

        // Output:
        // INFO: Total revenue: 2796.48
        // INFO: Average order value: 932.16
    }
}