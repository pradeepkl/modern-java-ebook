// Java 16+
/**
 * Listing 16.1 — StrategyPattern.java
 * Demonstrates: Strategy pattern using Function<T,R> as configurable behaviour
 * Chapter 16: Design Patterns Reimagined with Modern Java
 * Requires: Java 16+
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

    // Fixed structure: the Order record — the moving part is the discount rule
    record Order(String orderId, String customerId,
            double amount, String status, String region) {}

    public static void main(String[] args) {

        var order = new Order("ORD-001", "C1",
                600.0, "PENDING", "UK");

        // Each strategy is a Function — behaviour as a value
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

        // Registry — select strategy by tier at runtime; call site never changes
        var strategies = Map.of(
                "PREMIUM",  premiumDiscount,
                "SEASONAL", seasonalDiscount,
                "BULK",     bulkDiscount,
                "NONE",     noDiscount);

        // Fixed call site: rule.apply(order) — only the rule varies
        var rule = strategies.getOrDefault("PREMIUM", noDiscount);
        var discounted = rule.apply(order);

        log.info("After discount: £" + discounted.amount());

        // Compose two strategies — the composition is itself a strategy
        var combined = premiumDiscount.andThen(bulkDiscount);
        var finalPrice = combined.apply(order);

        log.info("Combined: £" + finalPrice.amount());

        // Output:
        // After discount: £510.0
        // Combined: £408.0
    }
}