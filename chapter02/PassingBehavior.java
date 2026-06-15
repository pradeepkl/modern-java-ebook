// Java 25+
// Feature shown: passing behavior via functional interfaces, final in Java 8+

/**
 * Listing 2.4 — PassingBehavior.java
 * Demonstrates: passing behavior via functional interfaces using Predicate
 * Chapter 2: Writing Java the Modern Way
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter02;

import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class PassingBehavior {

    private static final Logger log =
            Logger.getLogger(PassingBehavior.class.getName());

    // Record to represent a simple order domain object
    record Order(String id, String status, double amount) {}

    // Caller supplies the filtering rule — method supplies the structure
    static List<Order> filter(
            List<Order> orders,
            Predicate<Order> rule) {
        return orders.stream()
                .filter(rule)   // behavior injected by caller
                .toList();
    }

    void main() {
        var orders = List.of(
                new Order("ORD-001", "CONFIRMED", 120.0),
                new Order("ORD-002", "CANCELLED", 45.0),
                new Order("ORD-003", "CONFIRMED", 310.0));

        // Lambda supplies the filtering rule at the call site
        var confirmed = filter(orders,
                o -> o.status().equals("CONFIRMED"));

        // Different rule, same method structure
        var highValue = filter(orders,
                o -> o.amount() > 100.0);

        log.info("Confirmed: " + confirmed);
        log.info("High value: " + highValue);

        // Output:
        // Confirmed: [Order[id=ORD-001, status=CONFIRMED, amount=120.0],
        //             Order[id=ORD-003, status=CONFIRMED, amount=310.0]]
        // High value: [Order[id=ORD-001, status=CONFIRMED, amount=120.0],
        //              Order[id=ORD-003, status=CONFIRMED, amount=310.0]]
    }
}