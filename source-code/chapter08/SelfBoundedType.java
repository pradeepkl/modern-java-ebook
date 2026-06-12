// Java 16+
/**
 * Listing 8.8 — SelfBoundedType.java
 * Demonstrates: Self-bounded generic types for fluent builder APIs
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 16+
 */
package chapter08;

import java.time.Instant;
import java.util.logging.Logger;

public class SelfBoundedType {

    private static final Logger log = Logger.getLogger(SelfBoundedType.class.getName());

    // Domain record built by the fluent builder
    record OrderRequest(
            String referenceId,
            String submittedBy,
            Instant submittedAt,
            double amount,
            String notes) {}

    // Self-bounded base builder: T must extend RequestBuilder<T>
    abstract static class RequestBuilder<T extends RequestBuilder<T>> {
        protected String referenceId;
        protected String submittedBy;

        @SuppressWarnings("unchecked")
        public T withReferenceId(String id) {
            this.referenceId = id;
            return (T) this; // cast is safe — T is always the concrete subtype
        }

        @SuppressWarnings("unchecked")
        public T withSubmittedBy(String by) {
            this.submittedBy = by;
            return (T) this; // returns the concrete subtype, not RequestBuilder
        }
    }

    // OrderBuilder declares itself as T — concrete type is preserved in chain
    static class OrderBuilder extends RequestBuilder<OrderBuilder> {
        private double amount;

        public OrderBuilder withAmount(double amount) {
            this.amount = amount;
            return this; // concrete type preserved throughout the chain
        }

        public OrderRequest build() {
            return new OrderRequest(referenceId, submittedBy, Instant.now(), amount, "");
        }
    }

    public static void main(String[] args) {
        // Fluent chain — every method returns OrderBuilder, not the base type
        OrderRequest order = new OrderBuilder()
                .withReferenceId("ORD-001")  // returns OrderBuilder
                .withSubmittedBy("Alice")    // returns OrderBuilder
                .withAmount(99.99)           // returns OrderBuilder
                .build();                    // returns OrderRequest

        log.info("Reference : " + order.referenceId());
        log.info("Submitted : " + order.submittedBy());
        log.info("Amount    : " + order.amount());
        log.info("At        : " + order.submittedAt());

        // Output:
        // Reference : ORD-001
        // Submitted : Alice
        // Amount    : 99.99
        // At        : 2024-...
    }
}