// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 13.1 — ImperativeVsDeclarative.java
 * Demonstrates: Imperative iterator-based removal vs declarative removeIf
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter13;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class ImperativeVsDeclarative {

    private static final Logger LOG =
            Logger.getLogger(ImperativeVsDeclarative.class.getName());

    record Order(String id, String status) {}

    void main() {
        // Build a mutable list of orders with mixed statuses
        List<Order> imperativeOrders = new ArrayList<>(List.of(
                new Order("A1", "CONFIRMED"),
                new Order("A2", "PENDING"),
                new Order("A3", "CONFIRMED"),
                new Order("A4", "CANCELLED")
        ));

        // NOT IDEAL: Imperative — mechanics dominate intent
        // The developer manages cursor, test, and removal explicitly
        Iterator<Order> it = imperativeOrders.iterator();
        while (it.hasNext()) {
            Order order = it.next();
            if (!order.status().equals("CONFIRMED")) {
                it.remove(); // manual removal via iterator
            }
        }
        LOG.info("Imperative result: " + imperativeOrders);

        // Build a second identical list for the declarative approach
        List<Order> declarativeOrders = new ArrayList<>(List.of(
                new Order("A1", "CONFIRMED"),
                new Order("A2", "PENDING"),
                new Order("A3", "CONFIRMED"),
                new Order("A4", "CANCELLED")
        ));

        // Declarative — intent is the code
        // removeIf expresses what to remove, not how to iterate
        declarativeOrders.removeIf(o -> !o.status().equals("CONFIRMED"));
        LOG.info("Declarative result: " + declarativeOrders);

        // Output:
        // Imperative result: [Order[id=A1, status=CONFIRMED], Order[id=A3, status=CONFIRMED]]
        // Declarative result: [Order[id=A1, status=CONFIRMED], Order[id=A3, status=CONFIRMED]]
    }
}