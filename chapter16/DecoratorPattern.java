// Java 25+
// Feature shown: Decorator pattern via Function.andThen composition, final in Java 16+

/**
 * Listing 16.5 — DecoratorPattern.java
 * Demonstrates: Decorator pattern via Function.andThen composition
 * Chapter 16: Design Patterns Reimagined with Modern Java
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter16;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.logging.Logger;

public class DecoratorPattern {

    private static final Logger log =
            Logger.getLogger(MethodHandles.lookup()
                    .lookupClass().getName());

    // Order record — immutable value object for the pipeline
    record Order(String orderId, String customerId,
                 double amount, String status, String region) {}

    // Fixed: the andThen composition structure
    // Variable: each transformation as Function<Order, Order>

    void main() {

        var order = new Order("ORD-001", "C1",
                500.0, "PENDING", "UK");

        Function<Order, Order> confirm =
                o -> new Order(o.orderId(), o.customerId(),
                        o.amount(), "CONFIRMED", o.region());

        Function<Order, Order> audit =
                o -> { log.info(
                        "AUDIT: " + o.orderId()); return o; };

        Function<Order, Order> addVat =
                o -> new Order(o.orderId(), o.customerId(),
                        o.amount() * 1.20, o.status(), o.region());

        Function<Order, Order> metrics =
                o -> { log.info(
                        "METRICS: confirmed"); return o; };

        // Static pipeline — andThen chains the decorators
        var pipeline = confirm
                .andThen(audit)
                .andThen(metrics)
                .andThen(addVat);

        var result = pipeline.apply(order);
        log.info("Final: GBP" + result.amount()
                + " " + result.status());

        // Dynamic pipeline — decorators assembled at runtime
        var decorators = new ArrayList<Function<Order, Order>>();
        decorators.add(confirm);
        decorators.add(audit);   // toggle: auditEnabled
        decorators.add(addVat);  // toggle: vatApplicable

        var dynamic = decorators.stream()
                .reduce(Function.identity(), Function::andThen);

        var dynamicResult = dynamic.apply(order);
        log.info("Dynamic: GBP" + dynamicResult.amount());

        // Output:
        // AUDIT: ORD-001
        // METRICS: confirmed
        // Final: GBP600.0 CONFIRMED
        // AUDIT: ORD-001
        // Dynamic: GBP600.0
    }
}