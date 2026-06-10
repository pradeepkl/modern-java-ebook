// Java 22+
/**
 * Listing 6.13 — UnnamedPatterns.java
 * Demonstrates: Unnamed patterns (_) to discard unneeded record components
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 22+ with --enable-preview
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

    // Only country matters for routing — _ discards unneeded components
    public static String routingRegion(OrderEvent event) {
        return switch (event) {
            case OrderPlaced(
                    Order(var ignored1, Customer(var ignored2, var ignored3,
                          Address(var ignored4, var ignored5, var country)), var ignored6)) ->
                    "placed-" + country.toLowerCase();  // only country extracted
            case OrderShipped(var ignored, var tracking) ->
                    "shipped-" + tracking;              // order component discarded
        };
    }

    // Only orderId needed for audit log
    public static void logOrderId(OrderEvent event) {
        if (event instanceof OrderPlaced(Order(var id, var ignored1, var ignored2))) {
            log.info("Placed: " + id);  // customer and amount discarded
        }
    }

    public static void main(String[] args) {
        Address addr = new Address("1 Main St", "Berlin", "Germany");
        Customer customer = new Customer("C1", "Alice", addr);
        Order order = new Order("ORD-001", customer, 149.99);

        OrderEvent placed = new OrderPlaced(order);
        OrderEvent shipped = new OrderShipped(order, "TRACK-XYZ");

        // Demonstrate routingRegion with suppressed-variable patterns
        log.info(routingRegion(placed));   // placed-germany
        log.info(routingRegion(shipped));  // shipped-TRACK-XYZ

        // Demonstrate logOrderId — only id is bound, rest ignored
        logOrderId(placed);   // Placed: ORD-001
        logOrderId(shipped);  // no output — not an OrderPlaced

        // Output:
        // INFO: placed-germany
        // INFO: shipped-TRACK-XYZ
        // INFO: Placed: ORD-001
    }
}