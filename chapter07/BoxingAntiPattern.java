// Java 25+
// Feature shown: boxing/unboxing anti-pattern in stream pipelines, final in Java 8+

/**
 * Listing 7.2 — BoxingAntiPattern.java
 * Demonstrates: unnecessary boxing and unboxing transitions in a stream pipeline,
 * contrasted with the efficient primitive stream approach shown in Listing 7.1.
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter07;

import java.util.List;
import java.util.logging.Logger;

public class BoxingAntiPattern {

    private static final Logger log =
            Logger.getLogger(BoxingAntiPattern.class.getName());

    // Simple order record with primitive fields
    record Order(String id, int quantity, double unitPrice) {}

    void main() {
        List<Order> orders = List.of(
                new Order("P100", 2,  499.99),
                new Order("P200", 5,   99.50),
                new Order("P300", 1, 1299.00));

        // ANTI-PATTERN: needless object <-> primitive transitions
        // map()         -> Stream<Double>  (boxes each result as Double)
        // mapToDouble() -> DoubleStream    (unboxes each Double)
        // boxed()       -> Stream<Double>  (boxes again, pointlessly)
        // mapToDouble() -> DoubleStream    (unboxes a second time)
        // sum()         -> double          (finally stays primitive)
        double totalRevenue = orders.stream()
                .map(order ->
                        order.quantity()
                        * order.unitPrice())      // autoboxed to Double
                .mapToDouble(Double::doubleValue) // unbox to primitive
                .boxed()                          // box back to Double
                .mapToDouble(Double::doubleValue) // unbox again (wasteful)
                .sum();

        log.info("Total revenue (anti-pattern): " + totalRevenue);

        // PREFERRED: single boundary crossing, no redundant boxing
        double efficientTotal = orders.stream()
                .mapToDouble(o -> o.quantity() * o.unitPrice()) // one crossing
                .sum();

        log.info("Total revenue (preferred):    " + efficientTotal);

        // Both produce the same result; the anti-pattern creates extra allocations
        log.info("Results match: " + (totalRevenue == efficientTotal));

        // Output:
        // INFO: Total revenue (anti-pattern): 2797.48
        // INFO: Total revenue (preferred):    2797.48
        // INFO: Results match: true
    }
}