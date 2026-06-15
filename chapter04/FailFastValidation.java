// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 4.3 — FailFastValidation.java
 * Demonstrates: fail-fast validation at method boundaries using
 * IllegalArgumentException to detect invalid state early
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter04;

import java.util.List;
import java.util.logging.Logger;

public class FailFastValidation {

    private static final Logger LOG =
            Logger.getLogger(FailFastValidation.class.getName());

    // Minimal Order record to support the demonstration
    record Order(List<String> items) {
        List<String> getItems() {
            return items;
        }
    }

    // Fail-fast: validate inputs at the boundary before any processing
    public void processOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null");           // guard: null check
        }
        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "Order must contain at least one item"); // guard: empty check
        }
        // Invariants satisfied — proceed with processing
        LOG.info("Processing order with " + order.getItems().size() + " item(s)");
    }

    void main() {
        FailFastValidation validator = new FailFastValidation();

        // Valid order — processing proceeds normally
        Order valid = new Order(List.of("Widget", "Gadget"));
        validator.processOrder(valid);

        // Null order — caught at the boundary
        try {
            validator.processOrder(null);
        } catch (IllegalArgumentException ex) {
            LOG.warning("Caught expected error: " + ex.getMessage());
        }

        // Empty order — caught at the boundary
        try {
            validator.processOrder(new Order(List.of()));
        } catch (IllegalArgumentException ex) {
            LOG.warning("Caught expected error: " + ex.getMessage());
        }

        // Output:
        // INFO: Processing order with 2 item(s)
        // WARNING: Caught expected error: Order cannot be null
        // WARNING: Caught expected error: Order must contain at least one item
    }
}