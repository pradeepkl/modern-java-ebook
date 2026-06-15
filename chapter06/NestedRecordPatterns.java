// Java 25+
// Feature shown: unnamed variables and patterns (_) in nested record deconstruction, final in Java 22+
/**
 * Listing 6.12 — NestedRecordPatterns.java
 * Demonstrates: nested record pattern deconstruction with unnamed patterns (_)
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 22+ for unnamed patterns; compiled with --enable-preview --release 21
 * for the void main() instance main method
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

    // Audit an order event using nested record deconstruction
    static void audit(OrderEvent event) {
        switch (event) {
            // Nested deconstruction — Customer and Address deconstructed inside OrderPlaced
            case OrderPlaced(
                    Order(var id,
                          Customer(_, var name, Address(_, var city, _)),
                          var amount)) -> {
                log.info("Order " + id
                        + " placed by " + name
                        + " in " + city
                        + " for " + amount);
            }
            // Only orderId and trackingCode are needed; _ discards Customer
            case OrderShipped(Order(var id, _, _), var tracking) -> {
                log.info("Order " + id + " shipped: " + tracking);
            }
        }
    }

    void main() {
        Address addr = new Address("10 High St", "London", "UK");
        Customer customer = new Customer("C42", "Alice", addr);
        Order order1 = new Order("ORD-001", customer, 149.99);
        Order order2 = new Order("ORD-002", customer, 89.50);

        audit(new OrderPlaced(order1));
        audit(new OrderShipped(order2, "TRACK-XYZ-789"));

        // Output:
        // INFO: Order ORD-001 placed by Alice in London for 149.99
        // INFO: Order ORD-002 shipped: TRACK-XYZ-789
    }
}