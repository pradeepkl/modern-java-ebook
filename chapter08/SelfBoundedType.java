// Java 25+
// Feature shown: self-bounded generic types for fluent builder APIs, final in Java 16+

/**
 * Listing 8.8 — SelfBoundedType.java
 * Demonstrates: Self-bounded type parameter T extends Builder&lt;T&gt; to preserve
 * the concrete subtype through a fluent builder chain without casting at the call site.
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter08;

import java.time.Instant;
import java.util.logging.Logger;

// Immutable result record produced by the builder
record OrderRequest(
        String referenceId,
        String submittedBy,
        Instant submittedAt,
        double amount,
        String notes) {}

// Self-bounded base builder: T must be the concrete subtype
abstract class RequestBuilder<T extends RequestBuilder<T>> {

    protected String referenceId;
    protected String submittedBy;

    // Returns T — the compiler resolves this to the concrete subtype
    @SuppressWarnings("unchecked")
    public T withReferenceId(String id) {
        this.referenceId = id;
        return (T) this; // safe: T is always the concrete subtype
    }

    @SuppressWarnings("unchecked")
    public T withSubmittedBy(String by) {
        this.submittedBy = by;
        return (T) this;
    }
}

// OrderBuilder declares itself as T — concrete type is preserved
class OrderBuilder extends RequestBuilder<OrderBuilder> {

    private double amount;

    public OrderBuilder withAmount(double amount) {
        this.amount = amount;
        return this; // returns OrderBuilder, not the base type
    }

    public OrderRequest build() {
        return new OrderRequest(referenceId, submittedBy, Instant.now(), amount, "");
    }
}

public class SelfBoundedType {

    private static final Logger log = Logger.getLogger(SelfBoundedType.class.getName());

    void main() {
        // Every method in the chain returns OrderBuilder — no type is lost
        OrderRequest order = new OrderBuilder()
                .withReferenceId("ORD-001")  // returns OrderBuilder
                .withSubmittedBy("Alice")    // returns OrderBuilder
                .withAmount(99.99)           // returns OrderBuilder
                .build();                    // returns OrderRequest

        log.info("referenceId  : " + order.referenceId());
        log.info("submittedBy  : " + order.submittedBy());
        log.info("amount       : " + order.amount());
        log.info("submittedAt  : " + order.submittedAt());

        // Output:
        // referenceId  : ORD-001
        // submittedBy  : Alice
        // amount       : 99.99
        // submittedAt  : 2025-...
    }
}