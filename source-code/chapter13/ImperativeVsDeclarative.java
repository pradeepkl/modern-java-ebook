// Java 8+
/**
 * Listing 13.1 — ImperativeVsDeclarative.java
 * Demonstrates: Imperative iteration vs declarative removeIf
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 8+
 */
package chapter13;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class ImperativeVsDeclarative {

    private static final Logger LOG =
            Logger.getLogger(ImperativeVsDeclarative.class.getName());

    // Simple record to represent an order with a status
    record Order(String id, String status) {}

    public static void main(String[] args) {

        // Seed data: mix of CONFIRMED and PENDING orders
        List<Order> imperativeOrders = new ArrayList<>(List.of(
                new Order("A1", "CONFIRMED"),
                new Order("A2", "PENDING"),
                new Order("A3", "CONFIRMED"),
                new Order("A4", "CANCELLED"),
                new Order("A5", "CONFIRMED")
        ));

        // NOT IDEAL: Imperative — mechanics dominate intent
        // Must manage cursor, advancement, and removal explicitly
        Iterator<Order> it = imperativeOrders.iterator();
        while (it.hasNext()) {
            Order order = it.next();          // manually advance
            if (!order.status().equals("CONFIRMED")) {
                it.remove();                  // manually remove via iterator
            }
        }

        LOG.info("Imperative result: " + imperativeOrders);

        // Reset data for declarative example
        List<Order> declarativeOrders = new ArrayList<>(List.of(
                new Order("A1", "CONFIRMED"),
                new Order("A2", "PENDING"),
                new Order("A3", "CONFIRMED"),
                new Order("A4", "CANCELLED"),
                new Order("A5", "CONFIRMED")
        ));

        // Declarative — intent is the code
        // removeIf expresses WHAT to remove, not HOW to iterate
        declarativeOrders.removeIf(o -> !o.status().equals("CONFIRMED"));

        LOG.info("Declarative result: " + declarativeOrders);

        // Output:
        // Imperative result: [Order[id=A1, status=CONFIRMED], Order[id=A3, status=CONFIRMED], Order[id=A5, status=CONFIRMED]]
        // Declarative result: [Order[id=A1, status=CONFIRMED], Order[id=A3, status=CONFIRMED], Order[id=A5, status=CONFIRMED]]
    }
}