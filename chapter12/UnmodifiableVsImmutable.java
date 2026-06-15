// Java 25+
// Feature shown: unmodifiable views vs immutable copies, final in Java 9+
/**
 * Listing 12.8 — UnmodifiableVsImmutable.java
 * Demonstrates: the difference between an unmodifiable view and a true
 * immutable copy, showing that a view reflects mutations to the backing
 * list while List.copyOf() produces a snapshot that does not change.
 * Chapter 12: Collections, Ownership, and State Safety
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class UnmodifiableVsImmutable {

    private static final Logger log =
            Logger.getLogger(UnmodifiableVsImmutable.class.getName());

    void main() {

        List<String> mutable = new ArrayList<>();
        mutable.add("PENDING");
        mutable.add("CONFIRMED");

        // Unmodifiable view — blocks writes through this reference,
        // but the underlying list can still change via mutable
        List<String> view = Collections.unmodifiableList(mutable);

        // Write through the view is blocked
        try {
            view.add("SHIPPED");
        } catch (UnsupportedOperationException e) {
            log.info("Cannot modify through view");
        }

        // Write through the original reference succeeds
        mutable.add("SHIPPED");

        // The view reflects the mutation — it is not truly immutable
        log.info("View contents: " + view);

        // Immutable copy — snapshot taken at this moment, no link back
        List<String> immutable = List.copyOf(mutable); // Java 10+

        // Further mutation of the source list
        mutable.add("DELIVERED");

        // The immutable copy is unaffected by the mutation above
        log.info("Immutable contents: " + immutable);

        // Output:
        // INFO: Cannot modify through view
        // INFO: View contents: [PENDING, CONFIRMED, SHIPPED]
        // INFO: Immutable contents: [PENDING, CONFIRMED, SHIPPED]
    }
}