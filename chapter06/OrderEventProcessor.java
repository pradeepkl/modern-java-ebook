// Java 25+
// Feature shown: pattern matching for switch with record deconstruction and unnamed patterns, final in Java 22+

/**
 * Listing 6.18 — OrderEventProcessor.java
 * Demonstrates: switch pattern matching, guarded patterns, record deconstruction,
 *               unnamed patterns (_), and instanceof binding in streams
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 22+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
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
            case OrderPlaced(Order(var id, _, var amount)) when amount > 10_000 ->
                    "priority-fulfilment:" + id;
            // Standard placed order after high-value guard
            case OrderPlaced(Order(var id, _, _)) ->
                    "standard-fulfilment:" + id;
            // _ discards order details — warehouse is enough
            case OrderConfirmed(Order(var id, _, _), var warehouse) ->
                    "warehouse-" + warehouse + ":" + id;
            // Nested deconstruction extracts city for routing
            case OrderShipped(Order(_, Customer(_, _, Address(var city, _)), _),
                    var tracking, var carrier) ->
                    carrier + ":" + city + ":" + tracking;
            // Simple cancellation — only orderId needed
            case OrderCancelled(var id, _) ->
                    "cancelled:" + id;
        };
    }

    // Pattern binding in streams — fraud review queue
    public static void flagFraud(List<OrderEvent> events) {
        events.stream()
                .<String>mapMulti((event, downstream) -> {
                    if (event instanceof OrderCancelled(var id, var reason)
                            && reason.equals("FRAUD")) {
                        downstream.accept(id); // only fraud cancellations reach here
                    }
                })
                .forEach(id -> log.warning("Fraud review queued: " + id));
    }

    void main() {
        Address addr = new Address("Berlin", "DE");
        Customer cust = new Customer("C1", "Ada", addr);
        Order bigOrder = new Order("O1", cust, 15_000.0);
        Order smallOrder = new Order("O2", cust, 200.0);

        log.info(route(new OrderPlaced(bigOrder)));           // priority-fulfilment:O1
        log.info(route(new OrderPlaced(smallOrder)));         // standard-fulfilment:O2
        log.info(route(new OrderConfirmed(bigOrder, "WH9"))); // warehouse-WH9:O1
        log.info(route(new OrderShipped(bigOrder, "TRK42", "DHL"))); // DHL:Berlin:TRK42
        log.info(route(new OrderCancelled("O3", "CUSTOMER_REQUEST"))); // cancelled:O3

        List<OrderEvent> events = List.of(
                new OrderCancelled("O4", "FRAUD"),
                new OrderCancelled("O5", "DUPLICATE"),
                new OrderCancelled("O6", "FRAUD"));
        flagFraud(events); // warns for O4 and O6 only

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