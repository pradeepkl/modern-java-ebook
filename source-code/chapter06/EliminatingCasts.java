// Java 21+
package chapter06;

import java.util.logging.Logger;

/**
 * Listing 6.16 — EliminatingCasts.java
 * Demonstrates: Record pattern deconstruction in switch eliminates explicit casts
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 21+
 */
public class EliminatingCasts {

    private static final Logger log =
            Logger.getLogger(EliminatingCasts.class.getName());

    // Sealed hierarchy — compiler knows every permitted subtype
    sealed interface OrderEvent
            permits OrderPlaced, OrderConfirmed, OrderShipped, OrderCancelled {}

    record OrderPlaced(String orderId, String customerId, double amount)
            implements OrderEvent {}

    record OrderConfirmed(String orderId, String warehouseId)
            implements OrderEvent {}

    record OrderShipped(String orderId, String trackingCode, String carrier)
            implements OrderEvent {}

    record OrderCancelled(String orderId, String reason)
            implements OrderEvent {}

    // No cast. No instanceof. No ClassCastException risk.
    // Every field accessed is compiler-verified via record deconstruction.
    public static void process(OrderEvent event) {
        switch (event) {
            case OrderPlaced(var id, var customer, var amount) ->
                log.info("Placed: " + id + " customer=" + customer + " amount=" + amount);
            case OrderConfirmed(var id, var warehouse) ->
                log.info("Confirmed: " + id + " warehouse=" + warehouse);
            case OrderShipped(var id, var tracking, var carrier) ->
                log.info("Shipped: " + id + " via " + carrier + " tracking=" + tracking);
            case OrderCancelled(var id, var reason) ->
                log.warning("Cancelled: " + id + " reason=" + reason);
        }
    }

    public static void main(String[] args) {
        // Each event is processed without a single explicit cast
        process(new OrderPlaced("ORD-001", "CUST-42", 199.99));
        process(new OrderConfirmed("ORD-001", "WH-EAST"));
        process(new OrderShipped("ORD-001", "1Z999AA10123456784", "UPS"));
        process(new OrderCancelled("ORD-002", "Out of stock"));

        // Output:
        // INFO: Placed: ORD-001 customer=CUST-42 amount=199.99
        // INFO: Confirmed: ORD-001 warehouse=WH-EAST
        // INFO: Shipped: ORD-001 via UPS tracking=1Z999AA10123456784
        // WARNING: Cancelled: ORD-002 reason=Out of stock
    }
}