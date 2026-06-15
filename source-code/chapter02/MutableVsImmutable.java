// Java 25+
// Feature shown: immutability as the safer default, final in Java 25+

/**
 * Listing 2.6 — MutableVsImmutable.java
 * Demonstrates: the difference between mutable and immutable collections,
 * showing how mutable state can be destroyed by external code while
 * immutable snapshots preserve state regardless of caller behavior.
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

        // Pass the list to a method that clears it unexpectedly
        shareAndMutate(mutable);

        // Size is now 0 — internal state destroyed by external code
        log.info("After external mutation: " + mutable.size());

        // Immutable — snapshot cannot be modified by receiver
        var immutable = List.of(new Order("ORD-001", 99.99));

        // List.copyOf produces an unmodifiable defensive copy
        var snapshot = List.copyOf(immutable);

        // Size remains 1 — state preserved regardless of what callers do
        log.info("Snapshot size: " + snapshot.size());

        // Attempting to mutate an immutable list throws at runtime
        try {
            snapshot.clear(); // UnsupportedOperationException expected
        } catch (UnsupportedOperationException ex) {
            log.info("Immutable list rejected mutation: " + ex.getClass().getSimpleName());
        }

        // Output:
        // After external mutation: 0
        // Snapshot size: 1
        // Immutable list rejected mutation: UnsupportedOperationException
    }

    // Unintended side effect — clears the caller's list
    static void shareAndMutate(List<Order> orders) {
        orders.clear(); // mutates the caller's reference directly
    }
}