// Java 25+
// Feature shown: stream terminal operations (collection, scalar, string, file sinks), final in Java 16+

/**
 * Listing 14.16 — StreamSinks.java
 * Demonstrates: stream sinks — toList, toSet, joining, count, sum, max, file write
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter14;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StreamSinks {

    private static final Logger log = Logger.getLogger(StreamSinks.class.getName());

    record Order(String orderId, String region, double amount, String status) {}

    static List<Order> getOrders() {
        return List.of(
            new Order("ORD-001", "UK",  250.0, "CONFIRMED"),
            new Order("ORD-002", "US",  150.0, "CONFIRMED"),
            new Order("ORD-003", "UK",   50.0, "PENDING"),
            new Order("ORD-004", "EU",  400.0, "CONFIRMED")
        );
    }

    void main() throws IOException {
        List<Order> orders = getOrders();

        // toList() — unmodifiable List, Java 16+
        List<Order> resultList = orders.stream()
                .filter(o -> o.status().equals("CONFIRMED"))
                .toList();
        log.info("Confirmed count: " + resultList.size()); // 3

        // collect(Collectors.toSet()) — deduplicated regions
        Set<String> regionSet = orders.stream()
                .map(Order::region)
                .collect(Collectors.toSet());
        log.info("Regions: " + regionSet);

        // Collectors.joining — CSV of order IDs
        String csv = orders.stream()
                .map(Order::orderId)
                .collect(Collectors.joining(", "));
        log.info("CSV: " + csv); // ORD-001, ORD-002, ORD-003, ORD-004

        // joining with prefix and suffix
        String withBrackets = orders.stream()
                .map(Order::orderId)
                .collect(Collectors.joining(", ", "[", "]"));
        log.info("Bracketed: " + withBrackets); // [ORD-001, ORD-002, ORD-003, ORD-004]

        // count — scalar sink
        long ukCount = orders.stream()
                .filter(o -> o.region().equals("UK"))
                .count();
        log.info("UK orders: " + ukCount); // 2

        // sum — scalar sink via mapToDouble
        double total = orders.stream()
                .mapToDouble(Order::amount)
                .sum();
        log.info("Total amount: " + total); // 850.0

        // max — Optional scalar sink
        Optional<Order> highest = orders.stream()
                .max(Comparator.comparingDouble(Order::amount));
        highest.ifPresent(o -> log.info("Highest: " + o.orderId())); // ORD-004

        // File sink — write confirmed order IDs line by line
        Path outputFile = Path.of("processed-orders.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            orders.stream()
                    .filter(o -> o.status().equals("CONFIRMED"))
                    .map(Order::orderId)
                    .forEach(id -> {
                        try {
                            writer.write(id);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }
        log.info("Written to: " + outputFile.toAbsolutePath());

        // Output:
        // Confirmed count: 3
        // Regions: [EU, UK, US]
        // CSV: ORD-001, ORD-002, ORD-003, ORD-004
        // Bracketed: [ORD-001, ORD-002, ORD-003, ORD-004]
        // UK orders: 2
        // Total amount: 850.0
        // Highest: ORD-004
        // Written to: .../processed-orders.txt
    }
}