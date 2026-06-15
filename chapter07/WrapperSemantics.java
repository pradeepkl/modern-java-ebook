// Java 25+
// Feature shown: records with wrapper types for nullable semantics, final in Java 16+
/**
 * Listing 7.4 — WrapperSemantics.java
 * Demonstrates: wrapper types as domain modelling tools — null signals
 * "unknown" or "not yet set", while primitives signal a definite value.
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter07;

import java.time.LocalDate;
import java.util.logging.Logger;

public class WrapperSemantics {

    private static final Logger log =
            Logger.getLogger(WrapperSemantics.class.getName());

    // Wrapper fields are intentional here.
    // null quantity   -> value not yet confirmed
    // null discount   -> no discount negotiated
    // null isPriority -> status pending
    record CustomerOrder(
            String    orderId,
            Integer   quantity,
            Double    discountPercentage,
            Boolean   isPriority,
            LocalDate deliveryDate) {}

    // Primitive fields carry different semantics.
    // 0 quantity means zero items — not "unknown".
    record ConfirmedOrder(
            String  orderId,
            int     quantity,
            double  unitPrice,
            boolean isPriority) {}

    void main() {
        // Wrapper nulls model "not yet known" states explicitly
        CustomerOrder pending = new CustomerOrder(
                "ORD-001", null, null, null, null);

        log.info("Order ID   : " + pending.orderId());
        log.info("Quantity   : " + pending.quantity()           // null -> unconfirmed
                + " (null means unconfirmed)");
        log.info("Discount   : " + pending.discountPercentage() // null -> none negotiated
                + " (null means none negotiated)");
        log.info("Is priority: " + pending.isPriority()         // null -> status pending
                + " (null means status pending)");

        // Primitives carry definite values — zero means zero, not unknown
        ConfirmedOrder confirmed = new ConfirmedOrder(
                "ORD-002", 5, 199.99, true);

        log.info("Confirmed order: " + confirmed.orderId());
        log.info("Quantity : " + confirmed.quantity());   // 0 would mean zero items
        log.info("Unit price: " + confirmed.unitPrice());
        log.info("Priority : " + confirmed.isPriority());

        // Output:
        // Order ID   : ORD-001
        // Quantity   : null (null means unconfirmed)
        // Discount   : null (null means none negotiated)
        // Is priority: null (null means status pending)
        // Confirmed order: ORD-002
        // Quantity : 5
        // Unit price: 199.99
        // Priority : true
    }
}