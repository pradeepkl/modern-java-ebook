// Java 8+
/**
 * Listing 7.1 — PrimitiveStreamBoundary.java
 * Demonstrates: Crossing from object streams into primitive streams once
 *               to avoid repeated boxing/unboxing overhead during computation.
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 8+
 */
package chapter07;

import java.util.List;
import java.util.logging.Logger;

public class PrimitiveStreamBoundary {

    private static final Logger log =
            Logger.getLogger(PrimitiveStreamBoundary.class.getName());

    // Record lives in the object (reference) world — stored in a List
    record Order(String id, int quantity, double unitPrice) {}

    public static void main(String[] args) {

        // Object collection: reference types, heap-allocated
        List<Order> orders = List.of(
                new Order("P100", 2,  499.99),   // revenue:  999.98
                new Order("P200", 5,   99.50),   // revenue:  497.50
                new Order("P300", 1, 1299.00));  // revenue: 1299.00

        // Stream<Order>  → object world (reference pipeline)
        // mapToDouble()  → single boundary crossing into primitive world
        // sum()          → stays in DoubleStream; no boxing occurs here
        double totalRevenue = orders.stream()
                .mapToDouble(order ->
                        order.quantity() * order.unitPrice()) // int * double → double primitive
                .sum(); // terminal op on DoubleStream; returns primitive double

        // Contrast: using .map() + .reduce() would box each intermediate Double
        // mapToDouble() avoids that allocation pressure entirely
        log.info("Total revenue: " + totalRevenue);

        // Demonstrate individual primitive stream operations without boxing
        double maxUnitPrice = orders.stream()
                .mapToDouble(Order::unitPrice) // method reference, still one boundary crossing
                .max()                         // returns OptionalDouble (primitive-safe)
                .orElse(0.0);

        log.info("Most expensive unit price: " + maxUnitPrice);

        long orderCount = orders.stream()
                .mapToInt(Order::quantity)     // IntStream — no Integer boxing
                .filter(q -> q > 1)            // stays primitive throughout
                .count();                      // primitive long result

        log.info("Orders with quantity > 1: " + orderCount);

        // Output:
        // INFO: Total revenue: 2796.48
        // INFO: Most expensive unit price: 1299.0
        // INFO: Orders with quantity > 1: 2
    }
}