// Java 25+
// Feature shown: custom collectors with Collector.of(), final in Java 8+

/**
 * Listing 15.6 — CustomCollector.java
 * Demonstrates: building a custom Collector using Collector.of() with
 *               supplier, accumulator, combiner, and finisher functions
 * Chapter 15: Streams: Orchestrating Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter15;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CustomCollector {

    private static final Logger LOG = Logger.getLogger(CustomCollector.class.getName());

    record Order(String orderId, String customerId, String region, double amount, String status) {}

    record OrderSummary(int count, double total, boolean hasHighValue) {}

    static class SummaryAccumulator {
        int count = 0;
        double total = 0.0;
        boolean hasHighValue = false;

        void accept(Order order) {
            count++;
            total += order.amount();
            if (order.amount() > 500.0) hasHighValue = true;
        }

        SummaryAccumulator combine(SummaryAccumulator other) {
            count += other.count;
            total += other.total;
            hasHighValue = hasHighValue || other.hasHighValue;
            return this;
        }

        OrderSummary finish() {
            return new OrderSummary(count, total, hasHighValue);
        }
    }

    void main() {
        List<Order> orders = List.of(
            new Order("O1", "C1", "UK", 300.0, "SHIPPED"),
            new Order("O2", "C2", "UK", 750.0, "PENDING"),
            new Order("O3", "C3", "EU", 120.0, "SHIPPED"),
            new Order("O4", "C4", "EU", 600.0, "PENDING")
        );

        // Collector.of — four functions: supplier, accumulator, combiner, finisher
        Collector<Order, SummaryAccumulator, OrderSummary> summaryCollector = Collector.of(
                SummaryAccumulator::new,    // supplier: creates mutable container
                SummaryAccumulator::accept, // accumulator: adds one element
                SummaryAccumulator::combine,// combiner: merges two containers (parallel)
                SummaryAccumulator::finish);// finisher: converts container to result

        // Usage — same as any built-in collector
        OrderSummary ukSummary = orders.stream()
                .filter(o -> o.region().equals("UK"))
                .collect(summaryCollector);
        LOG.info("UK summary: " + ukSummary);

        // Used as a downstream collector inside groupingBy
        Map<String, OrderSummary> summaryByRegion = orders.stream()
                .collect(Collectors.groupingBy(Order::region, summaryCollector));
        summaryByRegion.forEach((region, summary) ->
                LOG.info("Region " + region + ": " + summary));

        // Output:
        // UK summary: OrderSummary[count=2, total=1050.0, hasHighValue=true]
        // Region EU: OrderSummary[count=2, total=720.0, hasHighValue=true]
        // Region UK: OrderSummary[count=2, total=1050.0, hasHighValue=true]
    }
}