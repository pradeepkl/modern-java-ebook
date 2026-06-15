// Java 25+
// Feature shown: Factory pattern via constructor references and records, final in Java 16+

/**
 * Listing 16.2 — FactoryPattern.java
 * Demonstrates: Factory pattern using constructor references stored in a registry map,
 *               with records as lightweight notifier implementations.
 * Chapter 16: Design Patterns Reimagined with Modern Java
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
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

    // Domain record — immutable order value
    record Order(String orderId, String customerId,
                 double amount, String status, String region) {}

    // Notifier interface — fixed call site: send(order, message)
    interface Notifier {
        void send(Order order, String message);
    }

    // Each record is a lightweight notifier implementation
    record EmailNotifier() implements Notifier {
        public void send(Order order, String message) {
            log.info("EMAIL -> " + order.customerId() + ": " + message);
        }
    }

    record SmsNotifier() implements Notifier {
        public void send(Order order, String message) {
            log.info("SMS -> " + order.customerId() + ": " + message);
        }
    }

    record PushNotifier() implements Notifier {
        public void send(Order order, String message) {
            log.info("PUSH -> " + order.customerId() + ": " + message);
        }
    }

    void main() {
        var order = new Order("ORD-001", "C1", 99.99, "CONFIRMED", "UK");

        // Registry — constructor references as values; adding a notifier = one map entry
        var registry = Map.<String, Supplier<Notifier>>of(
                "EMAIL", EmailNotifier::new,  // constructor reference, not an instance
                "SMS",   SmsNotifier::new,
                "PUSH",  PushNotifier::new);

        // Fixed call site — the type is the moving part, resolved at runtime
        List.of("EMAIL", "SMS", "PUSH").forEach(type -> {
            var notifier = registry
                    .getOrDefault(type,
                            () -> { throw new IllegalArgumentException(
                                    "Unknown notifier type: " + type); })
                    .get(); // invoke the constructor reference to produce an instance
            notifier.send(order, "Order confirmed.");
        });
        // Output:
        // EMAIL -> C1: Order confirmed.
        // SMS -> C1: Order confirmed.
        // PUSH -> C1: Order confirmed.
    }
}