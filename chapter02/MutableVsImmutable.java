// Java 25+
// Feature shown: immutability as the safer default, final in Java 21+
/**
 * Listing 2.6 — MutableVsImmutable.java
 * Demonstrates: mutable vs immutable collections and the risks of shared mutable state
 * Chapter 2: Writing Java the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter02;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MutableVsImmutable {

    private static final Logger log =
            Logger.getLogger(MutableVsImmutable.class.getName());

    // Immutable data carrier — components are final by default
    record Order(String id, double amount) {}

    void main() {
        // Mutable — caller and callee share the same backing list
        var mutable = new ArrayList<Order>();
        mutable.add(new Order("ORD-001", 99.99));
        log.info("Before external mutation: " + mutable.size()); // 1
        shareAndMutate(mutable);
        // Internal state destroyed by external code
        log.info("After external mutation: " + mutable.size()); // 0

        // Immutable — snapshot cannot be modified by receiver
        var immutable = List.of(new Order("ORD-001", 99.99));

        // List.copyOf produces an unmodifiable defensive copy
        var snapshot = List.copyOf(immutable);
        log.info("Snapshot size: " + snapshot.size()); // 1

        // Attempting to mutate an immutable list throws at runtime
        try {
            snapshot.add(new Order("ORD-002", 49.99));
        } catch (UnsupportedOperationException ex) {
            log.info("Mutation rejected: " + ex.getClass().getSimpleName());
        }

        // Output:
        // Before external mutation: 1
        // After external mutation: 0
        // Snapshot size: 1
        // Mutation rejected: UnsupportedOperationException
    }

    // Unintended side effect — mutates the caller's list
    static void shareAndMutate(List<Order> orders) {
        orders.clear(); // caller's reference now points to an empty list
    }
}