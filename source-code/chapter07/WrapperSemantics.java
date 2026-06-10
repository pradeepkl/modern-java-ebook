// Java 16+
/**
 * Listing 7.4 — WrapperSemantics.java
 * Demonstrates: Wrapper types as domain modelling tools — null semantics
 *               vs primitive types for confirmed, non-null values
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 16+
 */
package chapter07;

import java.time.LocalDate;
import java.util.logging.Logger;

public class WrapperSemantics {

    private static final Logger log = Logger.getLogger(
            WrapperSemantics.class.getName());

    // Wrapper fields are intentional here.
    // null quantity   → value not yet confirmed
    // null discount   → no discount negotiated
    // null isPriority → status pending
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

    public static void main(String[] args) {

        // Partially filled order — nulls model "not yet known"
        CustomerOrder draft = new CustomerOrder(
                "ORD-001", null, null, null, null);

        log.info("Draft order id: " + draft.orderId());
        log.info("Quantity confirmed: " + (draft.quantity() != null));
        log.info("Discount negotiated: " + (draft.discountPercentage() != null));
        log.info("Priority status known: " + (draft.isPriority() != null));

        // Fully confirmed order — primitives enforce non-null contract
        ConfirmedOrder confirmed = new ConfirmedOrder(
                "ORD-002", 5, 49.99, true);

        log.info("Confirmed order id: " + confirmed.orderId());
        log.info("Quantity: " + confirmed.quantity());   // 0 means zero, not unknown
        log.info("Unit price: " + confirmed.unitPrice());
        log.info("Is priority: " + confirmed.isPriority());

        // Output:
        // Draft order id: ORD-001
        // Quantity confirmed: false
        // Discount negotiated: false
        // Priority status known: false
        // Confirmed order id: ORD-002
        // Quantity: 5
        // Unit price: 49.99
        // Is priority: true
    }
}