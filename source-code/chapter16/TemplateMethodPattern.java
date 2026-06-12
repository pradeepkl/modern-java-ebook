// Java 16+
/**
 * Listing 16.4 — TemplateMethodPattern.java
 * Demonstrates: Template Method pattern using function parameters to inject
 *               variable steps into a fixed algorithm skeleton (filter→enrich→sort)
 * Chapter 16: Design Patterns Reimagined with Modern Java
 * Requires: Java 16+
 */
package chapter16;

import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class TemplateMethodPattern {

    private static final Logger log =
            Logger.getLogger(MethodHandles.lookup()
                    .lookupClass().getName());

    // Domain record — immutable order value object
    record Order(String orderId, String customerId,
                 double amount, String status, String region) {}

    // Fixed: the three-step pipeline sequence
    // Variable: filter, enrich, sort — all passed as functions
    static List<Order> process(
            List<Order> orders,
            Predicate<Order> filter,
            Function<Order, Order> enrich,
            Comparator<Order> sort) {

        return orders.stream()
                .filter(filter)   // step 1: select relevant orders
                .map(enrich)      // step 2: transform each order
                .sorted(sort)     // step 3: arrange the result
                .toList();
    }

    public static void main(String[] args) {

        var orders = List.of(
                new Order("ORD-001", "C1", 400.0, "CONFIRMED", "UK"),
                new Order("ORD-002", "C2", 150.0, "CONFIRMED", "US"),
                new Order("ORD-003", "C1", 600.0, "CONFIRMED", "UK"),
                new Order("ORD-004", "C3",  90.0, "PENDING",   "EU"));

        // Variation 1 — confirmed UK orders, VAT added, by amount desc
        var ukVat = process(
                orders,
                o -> o.status().equals("CONFIRMED") && o.region().equals("UK"),
                o -> new Order(o.orderId(), o.customerId(),
                        o.amount() * 1.20, o.status(), o.region()),
                Comparator.comparingDouble(Order::amount).reversed());

        ukVat.forEach(o -> log.info(o.orderId() + " £" + o.amount()));

        // Variation 2 — different behaviour, same fixed skeleton
        var allByCustomer = process(
                orders,
                o -> o.status().equals("CONFIRMED"),
                o -> o,  // identity — no enrichment
                Comparator.comparing(Order::customerId));

        allByCustomer.forEach(o -> log.info(o.customerId() + " " + o.orderId()));

        // Output:
        // INFO: ORD-003 £720.0
        // INFO: ORD-001 £480.0
        // INFO: C1 ORD-001
        // INFO: C1 ORD-003
        // INFO: C2 ORD-002
    }
}