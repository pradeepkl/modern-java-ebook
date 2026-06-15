// Java 25+
// Feature shown: bulk collection operations (forEach, removeIf, replaceAll), final in Java 8+

/**
 * Listing 13.3 — BulkOperations.java
 * Demonstrates: forEach, removeIf, and replaceAll as declarative bulk
 * operations on a mutable list, using internal iteration introduced in Java 8.
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter13;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BulkOperations {

    private static final Logger log = Logger.getLogger(BulkOperations.class.getName());

    record Order(String orderId, double amount, String status) {}

    void main() {

        // Build a mutable list — bulk operations require an owned, mutable list
        List<Order> orders = new ArrayList<>(List.of(
            new Order("ORD-001", 100.00, "CONFIRMED"),
            new Order("ORD-002", 250.00, "CANCELLED"),
            new Order("ORD-003",  75.00, "CONFIRMED"),
            new Order("ORD-004", 400.00, "PENDING"),
            new Order("ORD-005", 180.00, "CANCELLED")
        ));

        // forEach — apply a side effect to every element
        // Correct use: logging, notification, audit
        orders.forEach(o ->
                log.info("Processing: " + o.orderId()));

        // removeIf — remove elements matching a predicate
        // Modifies the list in place — use on owned lists only
        orders.removeIf(o ->
                o.status().equals("CANCELLED"));

        log.info("After removeIf (CANCELLED removed), size: " + orders.size());

        // replaceAll — replace every element with a transformed version
        // List size unchanged; each element is substituted in place
        orders.replaceAll(o ->
                new Order(o.orderId(),
                        o.amount() * 0.9,
                        o.status()));

        // Log final state to confirm discount was applied
        orders.forEach(o ->
                log.info("Final: " + o.orderId()
                        + " amount=" + o.amount()
                        + " status=" + o.status()));

        // Output:
        // Processing: ORD-001
        // Processing: ORD-002
        // Processing: ORD-003
        // Processing: ORD-004
        // Processing: ORD-005
        // After removeIf (CANCELLED removed), size: 3
        // Final: ORD-001 amount=90.0  status=CONFIRMED
        // Final: ORD-003 amount=67.5  status=CONFIRMED
        // Final: ORD-004 amount=360.0 status=PENDING
    }
}