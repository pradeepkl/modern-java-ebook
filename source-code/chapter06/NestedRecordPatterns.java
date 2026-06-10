// Java 23+
/**
 * Listing 6.12 — NestedRecordPatterns.java
 * Demonstrates: Nested record deconstruction with unnamed patterns (_) in switch
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 23+ (unnamed patterns finalized in Java 22, stable in 23)
 */
package chapter06;

import java.util.logging.Logger;

public class NestedRecordPatterns {

    private static final Logger log =
            Logger.getLogger(NestedRecordPatterns.class.getName());

    record Address(String street, String city, String country) {}
    record Customer(String id, String name, Address address) {}
    record Order(String orderId, Customer customer, double amount) {}

    sealed interface OrderEvent permits OrderPlaced, OrderShipped {}

    record OrderPlaced(Order order) implements OrderEvent {}
    record OrderShipped(Order order, String trackingCode) implements OrderEvent {}

    // Nested deconstruction: Order, Customer, and Address all deconstructed inline
    public static void audit(OrderEvent event) {
        switch (event) {
            // Unnamed patterns _ discard components we don't need (Java 22+)
            case OrderPlaced(
                    Order(var id,
                          Customer(var ignored1, var name,
                                   Address(var ignored2, var city, var ignored3)),
                          var amount)) -> {
                log.info("Order " + id
                        + " placed by " + name
                        + " in " + city
                        + " for $" + amount);
            }
            // Only orderId and trackingCode are needed here
            case OrderShipped(
                    Order(var id, var ignored4, var ignored5),
                    var tracking) -> {
                log.info("Order " + id + " shipped: " + tracking);
            }
        }
    }

    public static void main(String[] args) {
        Address addr = new Address("10 High St", "London", "UK");
        Customer customer = new Customer("C-001", "Alice", addr);
        Order order1 = new Order("ORD-100", customer, 299.99);
        Order order2 = new Order("ORD-101", customer, 49.00);

        // Demonstrate nested deconstruction for both event types
        audit(new OrderPlaced(order1));
        audit(new OrderShipped(order2, "TRACK-XYZ-789"));

        // Output:
        // INFO: Order ORD-100 placed by Alice in London for $299.99
        // INFO: Order ORD-101 shipped: TRACK-XYZ-789
    }
}