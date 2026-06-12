// Java 21+
/**
 * Listing 16.2 — FactoryPattern.java
 * Demonstrates: Factory pattern using constructor references in a registry map
 * Chapter 16: Design Patterns Reimagined with Modern Java
 * Requires: Java 21+
 */
package chapter16;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class FactoryPattern {

    private static final Logger log =
            Logger.getLogger(MethodHandles.lookup()
                    .lookupClass().getName());

    // Domain record — immutable order data
    record Order(String orderId, String customerId,
                 double amount, String status, String region) {}

    // Notifier interface — fixed call site: send()
    interface Notifier {
        void send(Order order, String message);
    }

    // Each notifier is a record — concise and immutable
    record EmailNotifier() implements Notifier {
        public void send(Order order, String message) {
            log.info("EMAIL → " + order.customerId() + ": " + message);
        }
    }

    record SmsNotifier() implements Notifier {
        public void send(Order order, String message) {
            log.info("SMS → " + order.customerId() + ": " + message);
        }
    }

    record PushNotifier() implements Notifier {
        public void send(Order order, String message) {
            log.info("PUSH → " + order.customerId() + ": " + message);
        }
    }

    public static void main(String[] args) {

        var order = new Order("ORD-001", "C1", 99.99, "CONFIRMED", "UK");

        // Registry — constructor references stored as Supplier<Notifier> values
        // Adding a new notifier type: add one map entry, nothing else changes
        var registry = Map.<String, Supplier<Notifier>>of(
                "EMAIL", EmailNotifier::new,   // constructor ref as value
                "SMS",   SmsNotifier::new,
                "PUSH",  PushNotifier::new);

        // Fixed call site — type string is the only moving part
        List.of("EMAIL", "SMS", "PUSH").forEach(type -> {
            var notifier = registry
                    .getOrDefault(type,
                            () -> { throw new IllegalArgumentException(
                                    "Unknown notifier type: " + type); })
                    .get(); // invoke the constructor
            notifier.send(order, "Order confirmed.");
        });

        // Output:
        // EMAIL → C1: Order confirmed.
        // SMS → C1: Order confirmed.
        // PUSH → C1: Order confirmed.
    }
}