// Java 25+
// Feature shown: wrapper types as domain modelling tools (Long id nullable), final in Java 8+
/**
 * Listing 7.5 — JpaEntityIdentifier.java
 * Demonstrates: Why JPA entity identifiers use Long (nullable) rather than long (primitive)
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter07;

import java.util.logging.Logger;

public class JpaEntityIdentifier {

    private static final Logger log = Logger.getLogger(
            JpaEntityIdentifier.class.getName());

    // Simulates a JPA entity — Long id is null before persistence
    static class Customer {

        // @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        // Long allows null: entity not yet persisted, id not yet assigned
        private Long id;

        private String name;

        Customer(String name) {
            this.id = null; // null signals: not yet saved to the database
            this.name = name;
        }

        // Called by the persistence layer after INSERT
        void assignId(long assignedId) {
            this.id = assignedId;
        }

        Long getId()     { return id; }
        String getName() { return name; }

        boolean isPersisted() {
            return id != null; // primitive long could never represent this
        }
    }

    void main() {
        Customer customer = new Customer("Alice");

        // Before persistence: id is null — not zero, not -1, genuinely absent
        log.info("Before save — id: " + customer.getId()
                + ", persisted: " + customer.isPersisted());

        // Simulate the database assigning a generated identity value
        customer.assignId(42L);

        // After persistence: id is now a real value
        log.info("After save  — id: " + customer.getId()
                + ", persisted: " + customer.isPersisted());

        // Demonstrate the null-check pattern common in JPA repositories
        Long id = customer.getId();
        if (id != null) {
            log.info("Customer with id " + id + " is managed by the persistence context");
        }

        // Output:
        // Before save -- id: null, persisted: false
        // After save  -- id: 42, persisted: true
        // Customer with id 42 is managed by the persistence context
    }
}