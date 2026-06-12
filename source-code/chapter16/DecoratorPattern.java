// Java 16+
/**
 * Listing 16.5 — DecoratorPattern.java
 * Demonstrates: Decorator pattern using Function.andThen() for pipeline composition
 * Chapter 16: Design Patterns Reimagined with Modern Java
 * Requires: Java 16+
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

    // Order record — immutable value object; each decorator returns a new instance
    record Order(String orderId, String customerId,
                 double amount, String status, String region) {}

    void main() {
        var order = new Order("ORD-001", "C1",
                500.0, "PENDING", "UK");

        // Fixed: the andThen composition structure
        // Variable: each transformation as Function<Order, Order>
        Function<Order, Order> confirm =
                o -> new Order(o.orderId(), o.customerId(),
                        o.amount(), "CONFIRMED", o.region());

        Function<Order, Order> audit =
                o -> { log.info("AUDIT: " + o.orderId()); return o; };

        Function<Order, Order> addVat =
                o -> new Order(o.orderId(), o.customerId(),
                        o.amount() * 1.20, o.status(), o.region());

        Function<Order, Order> metrics =
                o -> { log.info("METRICS: confirmed"); return o; };

        // Static pipeline — andThen chains the decorators in fixed order
        var pipeline = confirm
                .andThen(audit)
                .andThen(metrics)
                .andThen(addVat);

        var result = pipeline.apply(order);
        log.info("Final: \u00a3" + result.amount() + " " + result.status());
        // AUDIT: ORD-001
        // METRICS: confirmed
        // Final: £600.0 CONFIRMED

        // Dynamic pipeline — decorators assembled at runtime from a list
        var decorators = new ArrayList<Function<Order, Order>>();
        decorators.add(confirm);
        decorators.add(audit);   // toggle: auditEnabled
        decorators.add(addVat);  // toggle: vatApplicable

        // reduce folds the list into a single composed Function
        var dynamic = decorators.stream()
                .reduce(Function.identity(), Function::andThen);

        var dynamicResult = dynamic.apply(order);
        log.info("Dynamic: \u00a3" + dynamicResult.amount());
        // Dynamic: £600.0
    }

    public static void main(String[] args) {
        new DecoratorPattern().main();
    }
}