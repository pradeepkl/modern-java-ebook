// Java 8+
/**
 * Listing 8.13 — ConcurrentCollections.java
 * Demonstrates: ConcurrentHashMap atomic operations and BlockingQueue producer-consumer
 * Chapter 8: Concurrency Foundations and Coordination
 * Requires: Java 8+
 */
package chapter08;

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

    public static void main(String[] args) throws InterruptedException {

        // ConcurrentHashMap — concurrent reads and writes
        // Segments the map internally to reduce contention
        ConcurrentHashMap<String, Integer> orderCounts =
                new ConcurrentHashMap<>();

        // merge() is atomic — read-modify-write without external locking
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
                    eventQueue.put("event-" + i); // blocks if queue full
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
                    String event = eventQueue.take(); // blocks if queue empty
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
        // INFO: Produced: event-0 ... event-4
        // INFO: Consumed: event-0 ... event-4
    }
}