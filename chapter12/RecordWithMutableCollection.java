// Java 25+
// Feature shown: shallow vs deep immutability in records, final in Java 16+
/**
 * Listing 12.4 — RecordWithMutableCollection.java
 * Demonstrates: shallow vs deep immutability in records using List.copyOf
 * Chapter 12: Collections, Ownership, and State Safety
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter12;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RecordWithMutableCollection {

    private static final Logger log =
            Logger.getLogger(
                RecordWithMutableCollection.class.getName());

    // NOT IDEAL: Shallow immutability — the record reference cannot be
    // reassigned but the list inside it can be modified externally
    record OrderBatch(String batchId, List<String> orderIds) {}

    // Correct approach: Deep immutability — List.copyOf ensures
    // the internal list cannot be modified after construction
    record ImmutableOrderBatch(String batchId, List<String> orderIds) {
        ImmutableOrderBatch {
            // Compact constructor — validate and defensively copy
            orderIds = List.copyOf(orderIds);
        }
    }

    void main() {
        List<String> ids = new ArrayList<>();
        ids.add("ORD-001");
        ids.add("ORD-002");

        // Shallow record — external list mutation affects the record's state
        OrderBatch shallow = new OrderBatch("BATCH-1", ids);
        ids.add("ORD-003"); // mutates the list the record holds a reference to
        log.info("Shallow batch size: "
                + shallow.orderIds().size()); // 3, not 2

        // Deep record — compact constructor copies the list at construction time
        ImmutableOrderBatch deep =
                new ImmutableOrderBatch("BATCH-2", ids);
        ids.add("ORD-004"); // does NOT affect the deep record
        log.info("Deep batch size: "
                + deep.orderIds().size()); // still 3

        // Output:
        // INFO: Shallow batch size: 3
        // INFO: Deep batch size: 3
    }
}