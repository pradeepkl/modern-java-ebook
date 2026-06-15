// Java 25+
// Feature shown: Command pattern via records and generics, final in Java 16+

/**
 * Listing 16.8 — CommandPattern.java
 * Demonstrates: Command pattern using records, generics, lambdas, streams, and Optional
 * Chapter 16: Design Patterns Reimagined with Modern Java
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter16;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CommandPattern {

    private static final Logger log =
            Logger.getLogger(MethodHandles.lookup()
                    .lookupClass().getName());

    record Order(String orderId, String customerId,
            double amount, String status, String region) {}

    record OrderResult(String orderId,
            boolean success, String message) {}

    // Command<T> — fixed structure, variable behaviour
    // records (Ch 3) + generics (Ch 8) + lambdas (Ch 2)
    record Command<T>(
            String name,
            Supplier<T> execute,
            Runnable undo) {}

    static List<Command<OrderResult>> buildWorkflow(Order order) {
        return List.of(
                new Command<>("validate",
                        () -> new OrderResult(order.orderId(),
                                order.amount() > 0, "Validated"),
                        () -> log.info("Undo validate: " + order.orderId())),
                new Command<>("reserve-inventory",
                        () -> new OrderResult(order.orderId(),
                                true, "Reserved"),
                        () -> log.info("Released: " + order.orderId())),
                new Command<>("process-payment",
                        () -> new OrderResult(order.orderId(),
                                true, "Paid"),
                        () -> log.info("Refunded: " + order.orderId())),
                new Command<>("dispatch",
                        () -> new OrderResult(order.orderId(),
                                true, "Dispatched"),
                        () -> log.info("Recalled: " + order.orderId())));
    }

    void main() {
        var order = new Order("ORD-001", "C1", 299.99, "PENDING", "UK");
        var workflow = buildWorkflow(order);

        // Audit trail — streams + joining
        var trail = workflow.stream()
                .map(Command::name)
                .collect(Collectors.joining(" -> "));
        log.info("Workflow: " + trail);

        // Execute — stream pipeline, short-circuit on failure
        var failure = workflow.stream()
                .map(cmd -> cmd.execute().get())
                .filter(r -> !r.success())
                .findFirst();

        failure.ifPresentOrElse(
                f -> {
                    log.info("Failed: " + f.message());
                    // Undo in reverse order
                    new ArrayList<>(workflow)
                            .reversed()
                            .forEach(cmd -> cmd.undo().run());
                },
                () -> log.info("Complete: " + order.orderId()));

        // Output:
        // Workflow: validate -> reserve-inventory -> process-payment -> dispatch
        // Complete: ORD-001
    }
}