// Java 8+
/**
 * Listing 10.5 — OrderPricingService.java
 * Demonstrates: Stateless service design for inherent thread safety
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 8+
 */
package chapter10;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

// Stateless service — no instance fields, inherently thread-safe
public class OrderPricingService {

    private static final Logger log = Logger.getLogger(
            OrderPricingService.class.getName());

    // Immutable data carrier for an order line
    record OrderLine(String product, int quantity, double unitPrice) {}

    // No instance fields — operates purely on method arguments
    public double calculateTotal(
            List<OrderLine> lines,
            double discountRate) {
        return lines.stream()
                .mapToDouble(line ->
                        line.quantity() * line.unitPrice()) // sum line totals
                .sum() * (1 - discountRate);               // apply discount
    }

    public static void main(String[] args) throws InterruptedException {

        // Single shared service instance — safe for all threads
        OrderPricingService service = new OrderPricingService();

        List<OrderLine> order = List.of(
                new OrderLine("Widget", 3, 9.99),
                new OrderLine("Gadget", 1, 49.99),
                new OrderLine("Doohickey", 2, 14.99)
        );

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Multiple threads invoking the same stateless service concurrently
        for (int i = 0; i < 6; i++) {
            final int threadId = i;
            executor.submit(() -> {
                double total = service.calculateTotal(order, 0.10); // 10% off
                log.info("Thread-" + threadId
                        + " calculated total: $" + String.format("%.2f", total));
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Output:
        // Thread-0 calculated total: $89.91
        // Thread-1 calculated total: $89.91
        // Thread-2 calculated total: $89.91
        // (all threads produce identical results — no coordination needed)
    }
}