// Java 22+
/**
 * Listing 6.18 — OrderEventProcessor.java
 * Demonstrates: Switch pattern matching, guarded patterns, record deconstruction, unnamed patterns
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 22+ (preview features enabled)
 */
package chapter06;

import java.util.List;
import java.util.logging.Logger;

public class OrderEventProcessor {

    private static final Logger log =
            Logger.getLogger(OrderEventProcessor.class.getName());

    record Address(String city, String country) {}
    record Customer(String id, String name, Address address) {}
    record Order(String orderId, Customer customer, double amount) {}

    sealed interface OrderEvent
            permits OrderPlaced, OrderConfirmed, OrderShipped, OrderCancelled {}

    record OrderPlaced(Order order) implements OrderEvent {}
    record OrderConfirmed(Order order, String warehouseId) implements OrderEvent {}
    record OrderShipped(Order order, String trackingCode, String carrier) implements OrderEvent {}
    record OrderCancelled(String orderId, String reason) implements OrderEvent {}

    public static String route(OrderEvent event) {
        return switch (event) {
            // Guarded pattern — high-value orders routed to priority fulfilment
            case OrderPlaced(Order(var id, var ignored1, var amount)) when amount > 10_000 ->
                    "priority-fulfilment:" + id;
            // Standard placed order after high-value guard
            case OrderPlaced(Order(var id, var ignored2, var ignored3)) ->
                    "standard-fulfilment:" + id;
            // Discards order details — warehouse is enough
            case OrderConfirmed(Order(var id, var ignored4, var ignored5), var warehouse) ->
                    "warehouse-" + warehouse + ":" + id;
            // Nested deconstruction extracts city for routing
            case OrderShipped(
                    Order(var ignored6, Customer(var ignored7, var ignored8, Address(var city, var ignored9)), var ignored10),
                    var tracking, var carrier) ->
                    carrier + ":" + city + ":" + tracking;
            // Simple cancellation — only orderId needed
            case OrderCancelled(var id, var ignored11) ->
                    "cancelled:" + id;
        };
    }

    // Pattern binding in streams — fraud review queue
    public static void flagFraud(List<OrderEvent> events) {
        events.stream()
                .<String>mapMulti((event, downstream) -> {
                    // instanceof pattern binding extracts id and reason together
                    if (event instanceof OrderCancelled oc && oc.reason().equals("FRAUD")) {
                        downstream.accept(oc.orderId());
                    }
                })
                .forEach(id -> log.warning("Fraud review queued: " + id));
    }

    public static void main(String[] args) {
        Address addr = new Address("Berlin", "DE");
        Customer cust = new Customer("C1", "Alice", addr);
        Order bigOrder = new Order("O1", cust, 15_000);
        Order smallOrder = new Order("O2", cust, 200);

        log.info(route(new OrderPlaced(bigOrder)));
        log.info(route(new OrderPlaced(smallOrder)));
        log.info(route(new OrderConfirmed(bigOrder, "WH9")));
        log.info(route(new OrderShipped(bigOrder, "TRK42", "DHL")));
        log.info(route(new OrderCancelled("O3", "CUSTOMER_REQUEST")));

        List<OrderEvent> events = List.of(
                new OrderCancelled("O4", "FRAUD"),
                new OrderCancelled("O5", "DUPLICATE"),
                new OrderCancelled("O6", "FRAUD"));
        flagFraud(events);

        // Output:
        // INFO: priority-fulfilment:O1
        // INFO: standard-fulfilment:O2
        // INFO: warehouse-WH9:O1
        // INFO: DHL:Berlin:TRK42
        // INFO: cancelled:O3
        // WARNING: Fraud review queued: O4
        // WARNING: Fraud review queued: O6
    }
}