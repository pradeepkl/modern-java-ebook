// Java 21+
/**
 * Listing 16.7 — ChainOfResponsibility.java
 * Demonstrates: Chain of Responsibility as a stream pipeline of Functions
 * Chapter 16: Design Patterns Reimagined with Modern Java
 * Requires: Java 21+
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

    // Order record — the request travelling through the chain
    record Order(String orderId, String customerId,
                 double amount, String status, String region) {}

    // Result record — produced by whichever handler claims the request
    record OrderResult(String orderId,
                       String handler, String outcome) {}

    public static void main(String[] args) {

        var order = new Order("ORD-001", "C1", 1500.0, "PENDING", "UK");

        // Each handler: Function<Order, Optional<OrderResult>>
        // Returns present result if it handles; empty to pass down
        Function<Order, Optional<OrderResult>> standardHandler =
                o -> o.amount() <= 500.0
                        ? Optional.of(new OrderResult(
                                o.orderId(), "STANDARD",
                                "Approved up to £500"))
                        : Optional.empty();

        Function<Order, Optional<OrderResult>> seniorHandler =
                o -> o.amount() <= 2000.0
                        ? Optional.of(new OrderResult(
                                o.orderId(), "SENIOR",
                                "Approved up to £2000"))
                        : Optional.empty();

        Function<Order, Optional<OrderResult>> directorHandler =
                o -> Optional.of(new OrderResult(
                        o.orderId(), "DIRECTOR",
                        "Approved — escalated to director"));

        // Chain — stream + flatMap + findFirst IS the traversal
        var chain = List.of(standardHandler, seniorHandler, directorHandler);

        var result = chain.stream()
                .map(handler -> handler.apply(order))   // apply each handler
                .flatMap(Optional::stream)               // unwrap non-empty
                .findFirst();                            // short-circuit stop

        result.ifPresentOrElse(
                r -> log.info(r.handler() + " → " + r.outcome()),
                () -> log.info("No handler found"));

        // Extending the chain — add vipHandler without changing structure
        Function<Order, Optional<OrderResult>> vipHandler =
                o -> o.customerId().startsWith("VIP")
                        ? Optional.of(new OrderResult(
                                o.orderId(), "VIP", "Fast-tracked approval"))
                        : Optional.empty();

        var extendedChain = List.of(
                vipHandler, standardHandler, seniorHandler, directorHandler);

        extendedChain.stream()
                .map(h -> h.apply(order))
                .flatMap(Optional::stream)
                .findFirst()
                .ifPresent(r -> log.info(r.handler() + " → " + r.outcome()));

        // Output:
        // INFO: SENIOR → Approved up to £2000
        // INFO: SENIOR → Approved up to £2000
    }
}