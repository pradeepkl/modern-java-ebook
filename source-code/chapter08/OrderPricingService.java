// Java 8+
/**
 * Listing 8.3 — OrderPricingService.java
 * Demonstrates: Stateless service design for inherent thread safety
 * Chapter 8: Concurrency Foundations and Coordination
 * Requires: Java 8+
 */
package chapter08;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class OrderPricingService {

    private static final Logger log = Logger.getLogger(
            OrderPricingService.class.getName());

    // Immutable order line — safe to share across threads
    record OrderLine(int quantity, double unitPrice) {}

    // Stateless service — no instance fields, inherently thread-safe
    public double calculateTotal(
            List<OrderLine> lines,
            double discountRate) {
        return lines.stream()
                .mapToDouble(line ->
                        line.quantity() * line.unitPrice()) // sum line totals
                .sum() * (1 - discountRate);               // apply discount
    }

    public static void main(String[] args) throws InterruptedException {

        // Single shared service instance — safe because it is stateless
        OrderPricingService service = new OrderPricingService();

        List<OrderLine> order = List.of(
                new OrderLine(2, 49.99),   // 2 x £49.99
                new OrderLine(1, 199.99),  // 1 x £199.99
                new OrderLine(3, 9.99)     // 3 x £9.99
        );

        double discountRate = 0.10; // 10% discount

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Multiple threads invoking the same stateless service concurrently
        for (int i = 0; i < 6; i++) {
            final int taskId = i;
            executor.submit(() -> {
                double total = service.calculateTotal(order, discountRate);
                log.info("Task " + taskId + " — order total: £" +
                        String.format("%.2f", total));
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Output:
        // Task 0 — order total: £269.97
        // Task 1 — order total: £269.97
        // Task 2 — order total: £269.97
        // (all tasks produce identical, correct results — no coordination needed)
    }
}