// Java 9+
/**
 * Listing 10.8 — UnmodifiableVsImmutable.java
 * Demonstrates: Difference between unmodifiable views and truly immutable collections
 * Chapter 10: Collections, Ownership, and State Safety
 * Requires: Java 9+
 */
package chapter10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class UnmodifiableVsImmutable {

    private static final Logger log =
            Logger.getLogger(UnmodifiableVsImmutable.class.getName());

    public static void main(String[] args) {

        List<String> mutable = new ArrayList<>();
        mutable.add("PENDING");
        mutable.add("CONFIRMED");

        // Unmodifiable view — blocks modification through this reference,
        // but the underlying list can still change via mutable
        List<String> view = Collections.unmodifiableList(mutable);

        // Modification through the view is blocked
        try {
            view.add("SHIPPED"); // throws UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            log.info("Cannot modify through view"); // expected
        }

        // Modification through the original reference still succeeds
        mutable.add("SHIPPED");

        // The view reflects the change — it is a window onto mutable state
        log.info("View contents: " + view);

        // Immutable copy via List.copyOf — severs link to backing list
        List<String> immutable = List.copyOf(mutable);

        // Further mutation of the original does not affect the copy
        mutable.add("DELIVERED");

        // The immutable copy remains unchanged
        log.info("Immutable contents: " + immutable);

        // Attempting to modify the immutable copy is also blocked
        try {
            immutable.add("DELIVERED"); // throws UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            log.info("Cannot modify immutable copy");
        }

        // Output:
        // Cannot modify through view
        // View contents: [PENDING, CONFIRMED, SHIPPED]
        // Immutable contents: [PENDING, CONFIRMED, SHIPPED]
        // Cannot modify immutable copy
    }
}