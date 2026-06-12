// Java 21+
/**
 * Listing 16.6 — VisitorPattern.java
 * Demonstrates: Visitor pattern reimagined with sealed types and switch expressions
 * Chapter 16: Design Patterns Reimagined with Modern Java
 * Requires: Java 21+
 */
package chapter16;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Logger;

public class VisitorPattern {

    private static final Logger log =
            Logger.getLogger(MethodHandles.lookup()
                    .lookupClass().getName());

    // Sealed hierarchy — compiler enforces exhaustiveness in switch
    sealed interface DiscountRule
            permits PremiumRule, BulkRule, SeasonalRule, NoRule {}

    record PremiumRule(double percentage) implements DiscountRule {}
    record BulkRule(double threshold, double rate) implements DiscountRule {}
    record SeasonalRule(String season, double rate) implements DiscountRule {}
    record NoRule() implements DiscountRule {}

    // Domain record representing an order
    record Order(String orderId, String customerId,
                 double amount, String status, String region) {}

    // Fixed: the switch dispatch mechanism
    // Variable: the behaviour per sealed type
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

    // Second visitor: describe each rule — same dispatch, different behaviour
    static String describeRule(DiscountRule rule) {
        return switch (rule) {
            case PremiumRule p  -> "Premium " + (p.percentage() * 100) + "%";
            case BulkRule b     -> "Bulk above £" + b.threshold();
            case SeasonalRule s -> s.season() + " sale";
            case NoRule n       -> "No discount";
        };
    }

    public static void main(String[] args) {

        var order = new Order("ORD-001", "C1",
                600.0, "PENDING", "UK");

        var rules = List.of(
                new PremiumRule(0.15),
                new BulkRule(500.0, 0.10),
                new SeasonalRule("WINTER", 0.20),
                new NoRule());

        // Each rule dispatched through the same switch mechanism
        rules.forEach(rule ->
                log.info(String.format("%s → £%.2f",
                        describeRule(rule),
                        applyRule(order, rule))));

        // Output:
        // Premium 15.0% → £510.00
        // Bulk above £500.0 → £540.00
        // WINTER sale → £480.00
        // No discount → £600.00
    }
}