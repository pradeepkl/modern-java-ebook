// Java 25+
// Feature shown: pattern matching for switch with record deconstruction, final in Java 21+

/**
 * Listing 6.16 — EliminatingCasts.java
 * Demonstrates: Eliminating explicit casts using sealed interfaces and
 * record deconstruction patterns in switch expressions.
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 21+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter06;

import java.util.logging.Logger;

public class EliminatingCasts {

    private static final Logger log =
            Logger.getLogger(EliminatingCasts.class.getName());

    // Sealed hierarchy — compiler knows every permitted subtype
    sealed interface OrderEvent
            permits EliminatingCasts.OrderPlaced,
                    EliminatingCasts.OrderConfirmed,
                    EliminatingCasts.OrderShipped,
                    EliminatingCasts.OrderCancelled {}

    record OrderPlaced(String orderId,
            String customerId, double amount) implements OrderEvent {}

    record OrderConfirmed(String orderId,
            String warehouseId) implements OrderEvent {}

    record OrderShipped(String orderId,
            String trackingCode, String carrier) implements OrderEvent {}

    record OrderCancelled(String orderId,
            String reason) implements OrderEvent {}

    // No cast, no instanceof, no ClassCastException risk
    public static void process(OrderEvent event) {
        switch (event) {
            case OrderPlaced(var id, var customer, var amount) ->
                log.info("Placed: " + id
                        + " customer=" + customer
                        + " amount=" + amount);
            case OrderConfirmed(var id, var warehouse) ->
                log.info("Confirmed: " + id
                        + " warehouse=" + warehouse);
            case OrderShipped(var id, var tracking, var carrier) ->
                log.info("Shipped: " + id
                        + " via " + carrier
                        + " tracking=" + tracking);
            case OrderCancelled(var id, var reason) ->
                log.warning("Cancelled: " + id
                        + " reason=" + reason);
        }
    }

    void main() {
        process(new OrderPlaced("ORD-001", "CUST-42", 149.99));
        process(new OrderConfirmed("ORD-001", "WH-EAST"));
        process(new OrderShipped("ORD-001", "1Z999AA10123456784", "UPS"));
        process(new OrderCancelled("ORD-002", "Out of stock"));
        // Output:
        // INFO: Placed: ORD-001 customer=CUST-42 amount=149.99
        // INFO: Confirmed: ORD-001 warehouse=WH-EAST
        // INFO: Shipped: ORD-001 via UPS tracking=1Z999AA10123456784
        // WARNING: Cancelled: ORD-002 reason=Out of stock
    }
}