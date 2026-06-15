// Java 25+
// Feature shown: Chain of Responsibility via streams and Optional, final in Java 16+
/**
 * Listing 16.7 — ChainOfResponsibility.java
 * Demonstrates: Chain of Responsibility pattern using streams, Optional, and records
 * Chapter 16: Design Patterns Reimagined with Modern Java
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter16;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;

public class ChainOfResponsibility {

    private static final Logger log =
            Logger.getLogger(MethodHandles.lookup()
                    .lookupClass().getName());

    // Domain record representing an order request
    record Order(String orderId, String customerId,
            double amount, String status, String region) {}

    // Fixed: the stream traversal — filter + findFirst
    // Variable: each handler as Function<Order, Optional<OrderResult>>
    // A handler returns Optional.of(result) if it claims the request
    // or Optional.empty() to pass it to the next handler
    record OrderResult(String orderId,
            String handler, String outcome) {}

    void main() {

        var order = new Order("ORD-001", "C1",
                1500.0, "PENDING", "UK");

        // Each handler returns present result if it handles the order
        // Returns empty to pass responsibility down the chain
        Function<Order, Optional<OrderResult>> standardHandler =
                o -> o.amount() <= 500.0
                        ? Optional.of(new OrderResult(
                                o.orderId(), "STANDARD",
                                "Approved up to 500"))
                        : Optional.empty();

        Function<Order, Optional<OrderResult>> seniorHandler =
                o -> o.amount() <= 2000.0
                        ? Optional.of(new OrderResult(
                                o.orderId(), "SENIOR",
                                "Approved up to 2000"))
                        : Optional.empty();

        Function<Order, Optional<OrderResult>> directorHandler =
                o -> Optional.of(new OrderResult(
                        o.orderId(), "DIRECTOR",
                        "Approved - escalated to director"));

        // Chain — fixed traversal structure, variable handlers
        // stream + flatMap + findFirst IS the chain
        var chain = List.of(
                standardHandler,
                seniorHandler,
                directorHandler);

        var result = chain.stream()
                .map(handler -> handler.apply(order))  // apply each handler
                .flatMap(Optional::stream)              // unwrap non-empty results
                .findFirst();                           // stop at first match

        result.ifPresentOrElse(
                r -> log.info(r.handler() + " -> " + r.outcome()),
                () -> log.info("No handler found"));
        // Output: SENIOR -> Approved up to 2000

        // Adding a new handler — add to the list
        // The chain structure never changes
        Function<Order, Optional<OrderResult>> vipHandler =
                o -> o.customerId().startsWith("VIP")
                        ? Optional.of(new OrderResult(
                                o.orderId(), "VIP",
                                "Fast-tracked approval"))
                        : Optional.empty();

        var extendedChain = List.of(
                vipHandler,        // checked first
                standardHandler,
                seniorHandler,
                directorHandler);

        extendedChain.stream()
                .map(h -> h.apply(order))
                .flatMap(Optional::stream)
                .findFirst()
                .ifPresent(r ->
                        log.info(r.handler() + " -> " + r.outcome()));
        // Output: SENIOR -> Approved up to 2000
    }
}