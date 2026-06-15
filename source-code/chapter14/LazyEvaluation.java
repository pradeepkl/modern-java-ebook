// Java 25+
// Feature shown: stream lazy evaluation (intermediate vs terminal operations), final in Java 8+

/**
 * Listing 14.3 — LazyEvaluation.java
 * Demonstrates: Stream pipeline laziness — intermediate operations build a
 * description; execution begins only when the terminal operation is invoked.
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class LazyEvaluation {

    private static final Logger LOG = Logger.getLogger(LazyEvaluation.class.getName());

    record Order(String orderId, String status, double amount) {}

    void main() {
        List<Order> orders = List.of(
            new Order("ORD-001", "CONFIRMED", 120.00),
            new Order("ORD-002", "PENDING",   45.50),
            new Order("ORD-003", "CONFIRMED", 200.00),
            new Order("ORD-004", "CANCELLED",  30.00)
        );

        // This builds a description — nothing executes yet
        // No filtering, no mapping, no iteration
        Stream<Double> pipeline = orders.stream()
            .filter(o -> {
                LOG.info("filtering: " + o.orderId()); // logged only during terminal op
                return o.status().equals("CONFIRMED");
            })
            .map(o -> {
                LOG.info("mapping: " + o.orderId());   // logged only for passing elements
                return o.amount();
            });

        LOG.info("Pipeline built — no output yet");

        // Execution starts here — terminal operation
        // Only NOW does the pipeline process elements
        List<Double> amounts = pipeline.toList();

        LOG.info("Collected amounts: " + amounts);

        // Output:
        // Pipeline built — no output yet
        // filtering: ORD-001
        // mapping: ORD-001       (filter passed)
        // filtering: ORD-002     (filter failed — no mapping)
        // filtering: ORD-003
        // mapping: ORD-003       (filter passed)
        // filtering: ORD-004     (filter failed — no mapping)
        // Collected amounts: [120.0, 200.0]
        // map only runs for elements that passed filter
        // The pipeline is one pass, not two separate passes
    }
}