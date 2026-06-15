// Java 25+
// Feature shown: unnamed patterns and variables (_), final in Java 22+

/**
 * Listing 6.13 — UnnamedPatterns.java
 * Demonstrates: unnamed patterns (_) to discard unneeded record components
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 22+ for unnamed patterns; void main() final in Java 25+
 */
package chapter06;

import java.util.logging.Logger;

public class UnnamedPatterns {

    private static final Logger log =
            Logger.getLogger(UnnamedPatterns.class.getName());

    record Address(String street, String city, String country) {}
    record Customer(String id, String name, Address address) {}
    record Order(String orderId, Customer customer, double amount) {}

    sealed interface OrderEvent permits OrderPlaced, OrderShipped {}

    record OrderPlaced(Order order) implements OrderEvent {}
    record OrderShipped(Order order, String trackingCode) implements OrderEvent {}

    // Only the country matters for routing; _ discards unused components
    public static String routingRegion(OrderEvent event) {
        return switch (event) {
            case OrderPlaced(
                    Order(_, Customer(_, _, Address(_, _, var country)), _)) ->
                    "placed-" + country.toLowerCase();  // only country extracted
            case OrderShipped(_, var tracking) ->
                    "shipped-" + tracking;              // order component discarded
        };
    }

    // Only the orderId is needed for the audit log
    public static void logOrderId(OrderEvent event) {
        if (event instanceof OrderPlaced(Order(var id, _, _))) {
            log.info("Placed: " + id);  // customer and amount discarded with _
        }
    }

    void main() {
        Address addr = new Address("1 Main St", "Berlin", "DE");
        Customer cust = new Customer("C1", "Ada", addr);
        Order order = new Order("ORD-001", cust, 149.99);

        OrderEvent placed = new OrderPlaced(order);
        OrderEvent shipped = new OrderShipped(order, "TRACK-XYZ");

        // Demonstrate routingRegion with unnamed patterns
        log.info(routingRegion(placed));   // placed-de
        log.info(routingRegion(shipped));  // shipped-TRACK-XYZ

        // Demonstrate logOrderId — only orderId is bound, rest discarded
        logOrderId(placed);   // Placed: ORD-001
        logOrderId(shipped);  // no output — not an OrderPlaced

        // Output:
        // INFO: placed-de
        // INFO: shipped-TRACK-XYZ
        // INFO: Placed: ORD-001
    }
}