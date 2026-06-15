// Java 25+
// Feature shown: Visitor pattern via sealed types and switch pattern matching, final in Java 21+

/**
 * Listing 16.6 — VisitorPattern.java
 * Demonstrates: Visitor pattern reimagined with sealed interfaces and exhaustive
 * switch pattern matching, replacing convention-based dispatch with compiler-enforced
 * exhaustiveness across a sealed DiscountRule hierarchy.
 * Chapter 16: Design Patterns Reimagined with Modern Java
 * Requires: Java 21+ for pattern matching for switch (final);
 * void main() instance main method final in Java 25+ (JEP 512)
 */
package chapter16;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Logger;

public class VisitorPattern {

    private static final Logger log =
            Logger.getLogger(MethodHandles.lookup()
                    .lookupClass().getName());

    // Fixed: the switch dispatch mechanism
    // Variable: the behaviour per sealed type
    sealed interface DiscountRule
            permits PremiumRule, BulkRule,
                    SeasonalRule, NoRule {}

    record PremiumRule(double percentage) implements DiscountRule {}
    record BulkRule(double threshold, double rate) implements DiscountRule {}
    record SeasonalRule(String season, double rate) implements DiscountRule {}
    record NoRule() implements DiscountRule {}

    record Order(String orderId, String customerId,
                 double amount, String status, String region) {}

    static double applyRule(Order order, DiscountRule rule) {
        return switch (rule) {
            case PremiumRule p  -> order.amount() * (1 - p.percentage());
            case BulkRule b     -> order.amount() > b.threshold()
                    ? order.amount() * (1 - b.rate())
                    : order.amount();
            case SeasonalRule s -> order.amount() * (1 - s.rate());
            case NoRule n       -> order.amount();
            // No default — sealed hierarchy is exhaustive
        };
    }

    static String describeRule(DiscountRule rule) {
        return switch (rule) {
            case PremiumRule p  -> "Premium " + (p.percentage() * 100) + "%";
            case BulkRule b     -> "Bulk above GBP" + b.threshold();
            case SeasonalRule s -> s.season() + " sale";
            case NoRule n       -> "No discount";
        };
    }

    void main() {
        var order = new Order("ORD-001", "C1",
                600.0, "PENDING", "UK");

        var rules = List.of(
                new PremiumRule(0.15),
                new BulkRule(500.0, 0.10),
                new SeasonalRule("WINTER", 0.20),
                new NoRule());

        rules.forEach(rule ->
                log.info(String.format("%s -> GBP%.2f",
                        describeRule(rule),
                        applyRule(order, rule))));
        // Output:
        // Premium 15.0% -> GBP510.00
        // Bulk above GBP500.0 -> GBP540.00
        // WINTER sale -> GBP480.00
        // No discount -> GBP600.00
    }
}