// Java 25+
// Feature shown: ConcurrentHashMap and BlockingQueue for thread-safe collections, final in Java 8+

/**
 * Listing 10.13 — ConcurrentCollections.java
 * Demonstrates: ConcurrentHashMap for concurrent reads/writes and
 * LinkedBlockingQueue for producer-consumer coordination without
 * explicit synchronisation.
 * Chapter 10: Concurrency Foundations and Coordination
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter10;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ConcurrentCollections {

    private static final Logger log =
            Logger.getLogger(ConcurrentCollections.class.getName());

    void main() throws InterruptedException {

        // ConcurrentHashMap — segments internally to reduce contention
        // Multiple threads can write to different segments simultaneously
        ConcurrentHashMap<String, Integer> orderCounts =
                new ConcurrentHashMap<>();

        // merge is atomic — safe to call from multiple threads
        orderCounts.merge("PLACED", 1, Integer::sum);
        orderCounts.merge("PLACED", 1, Integer::sum);
        orderCounts.merge("SHIPPED", 1, Integer::sum);

        log.info("Placed: " + orderCounts.get("PLACED"));   // 2
        log.info("Shipped: " + orderCounts.get("SHIPPED")); // 1

        // BlockingQueue — producer-consumer coordination
        // put() blocks when full, take() blocks when empty
        BlockingQueue<String> eventQueue =
                new LinkedBlockingQueue<>(100);

        ExecutorService producer = Executors.newSingleThreadExecutor();
        ExecutorService consumer = Executors.newSingleThreadExecutor();

        // Producer — adds events to the queue
        producer.submit(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    eventQueue.put("event-" + i);
                    log.info("Produced: event-" + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Consumer — processes events from the queue
        consumer.submit(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    String event = eventQueue.take(); // blocks until available
                    log.info("Consumed: " + event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.shutdown();
        consumer.shutdown();
        producer.awaitTermination(5, TimeUnit.SECONDS);
        consumer.awaitTermination(5, TimeUnit.SECONDS);

        // Output:
        // INFO: Placed: 2
        // INFO: Shipped: 1
        // INFO: Produced: event-0
        // INFO: Consumed: event-0
        // INFO: Produced: event-1
        // INFO: Consumed: event-1
        // ... (order of produced/consumed lines may vary)
    }
}