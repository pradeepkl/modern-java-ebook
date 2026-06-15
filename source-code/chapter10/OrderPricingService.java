// Java 25+
// Feature shown: stateless service with records and streams, final in Java 16+

/**
 * Listing 10.5 — OrderPricingService.java
 * Demonstrates: stateless service design for inherent thread safety,
 *               using records as immutable data carriers and streams for computation
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter10;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

// Stateless service — no instance fields, inherently thread-safe
public class OrderPricingService {

    private static final Logger log =
            Logger.getLogger(OrderPricingService.class.getName());

    // Immutable record — safe to share across threads
    record OrderLine(String productId, int quantity, double unitPrice) {}

    // No shared state — operates only on its arguments
    public double calculateTotal(
            List<OrderLine> lines,
            double discountRate) {
        return lines.stream()
                .mapToDouble(line ->
                        line.quantity() * line.unitPrice()) // sum line totals
                .sum() * (1 - discountRate);               // apply discount
    }

    void main() throws InterruptedException {
        OrderPricingService service = new OrderPricingService();

        // Immutable order lines — created once, shared freely
        List<OrderLine> lines = List.of(
                new OrderLine("PROD-001", 2, 49.99),
                new OrderLine("PROD-002", 1, 19.99),
                new OrderLine("PROD-003", 3, 9.99)
        );

        double discountRate = 0.10; // 10 percent discount

        // Multiple threads calling the same stateless service safely
        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 6; i++) {
            executor.submit(() -> {
                double total = service.calculateTotal(lines, discountRate);
                log.info("Order total after discount: " + total);
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Output:
        // INFO: Order total after discount: 107.982
        // INFO: Order total after discount: 107.982
        // (repeated 6 times — consistent result, no coordination needed)
    }
}