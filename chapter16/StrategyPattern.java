// Java 25+
// Feature shown: Strategy pattern via Function<T,R> and records, final in Java 16+

/**
 * Listing 16.1 — StrategyPattern.java
 * Demonstrates: Strategy pattern implemented with Function&lt;T,R&gt; and records.
 * The call site is fixed; the discount rule is the variable part passed as a lambda.
 * Chapter 16: Design Patterns Reimagined with Modern Java
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter16;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

public class StrategyPattern {

    private static final Logger log =
            Logger.getLogger(MethodHandles.lookup()
                    .lookupClass().getName());

    // Fixed: the record structure — the domain object never changes shape
    record Order(String orderId, String customerId,
            double amount, String status, String region) {}

    void main() {

        var order = new Order("ORD-001", "C1",
                600.0, "PENDING", "UK");

        // Variable: each lambda is a distinct discount strategy
        Function<Order, Order> premiumDiscount =
                o -> new Order(o.orderId(), o.customerId(),
                        o.amount() * 0.85, o.status(), o.region());

        Function<Order, Order> seasonalDiscount =
                o -> new Order(o.orderId(), o.customerId(),
                        o.amount() * 0.90, o.status(), o.region());

        Function<Order, Order> bulkDiscount =
                o -> o.amount() > 500.0
                        ? new Order(o.orderId(), o.customerId(),
                                o.amount() * 0.80,
                                o.status(), o.region())
                        : o;

        Function<Order, Order> noDiscount = o -> o; // identity strategy

        // Registry: select strategy by tier at runtime; call site never changes
        var strategies = Map.of(
                "PREMIUM",  premiumDiscount,
                "SEASONAL", seasonalDiscount,
                "BULK",     bulkDiscount,
                "NONE",     noDiscount);

        var rule = strategies.getOrDefault("PREMIUM", noDiscount);
        var discounted = rule.apply(order); // fixed call site

        log.info("After discount: GBP " + discounted.amount());
        // After discount: GBP 510.0

        // Compose two strategies — the composition is itself a strategy
        var combined = premiumDiscount.andThen(bulkDiscount);
        var finalPrice = combined.apply(order);

        log.info("Combined: GBP " + finalPrice.amount());
        // Combined: GBP 408.0
    }
}