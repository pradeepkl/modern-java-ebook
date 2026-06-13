// Java 21+ (preview — instance main methods, JEP 445/463)
// Feature shown: records, final in Java 16+

/**
 * Listing 1.3 — ProductRecord.java
 * Demonstrates: records as concise, immutable data carriers with compiler-generated
 * constructor, accessors, equals, hashCode, and toString
 * Chapter 1: Modern Java: A Shift in Mindset
 * Requires: Java 21+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter01;

import java.util.logging.Logger;

// A record declaration: compiler generates constructor, accessors, equals, hashCode, toString
public record ProductRecord(
        String id, String name, double price, String category) {

    private static final Logger log =
            Logger.getLogger(ProductRecord.class.getName());

    // Compact canonical constructor for validation — no boilerplate required
    public ProductRecord {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name required");
        }
    }

    void main() {
        // Record instantiation — single line, no builder or factory needed
        var product = new ProductRecord(
                "P-001", "Wireless Keyboard", 49.99, "Electronics");

        // toString is auto-generated: ProductRecord[id=..., name=..., ...]
        log.info(product.toString());

        // Accessor methods are generated from component names — no getXxx() prefix
        log.info("Name : " + product.name());
        log.info("Price: " + product.price());

        // Output:
        // ProductRecord[id=P-001, name=Wireless Keyboard, price=49.99, category=Electronics]
        // Name : Wireless Keyboard
        // Price: 49.99
    }
}