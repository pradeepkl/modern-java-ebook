// Java 8+
/**
 * Listing 7.5 — JpaEntityIdentifier.java
 * Demonstrates: Why JPA entity identifiers use Long (wrapper) instead of long (primitive)
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 8+
 */
package chapter07;

import java.util.logging.Logger;

public class JpaEntityIdentifier {

    private static final Logger log = Logger.getLogger(
            JpaEntityIdentifier.class.getName());

    /**
     * Simulates a JPA-style entity where Long id can be null before persistence.
     * Using primitive long would default to 0, masking "not yet persisted" state.
     */
    static class Customer {

        // @Id @GeneratedValue — null before persistence, assigned after save
        private Long id;   // null = not yet persisted; 0L would be misleading

        private String name;

        Customer(String name) {
            this.id = null; // explicitly unassigned
            this.name = name;
        }

        // Simulates what a JPA provider does after INSERT
        void assignId(Long generatedId) {
            this.id = generatedId;
        }

        boolean isPersisted() {
            return id != null; // impossible to express with primitive long
        }

        Long getId()     { return id; }
        String getName() { return name; }
    }

    public static void main(String[] args) {

        Customer customer = new Customer("Alice");

        // Before persistence: id is null — not zero, not assigned
        log.info("Before save — id: " + customer.getId()
                + ", persisted: " + customer.isPersisted());

        // Simulate JPA assigning a generated primary key after INSERT
        customer.assignId(42L);

        // After persistence: id is a real value
        log.info("After save  — id: " + customer.getId()
                + ", persisted: " + customer.isPersisted());

        // Demonstrate why primitive long fails here
        long primitiveId = 0L; // default — indistinguishable from "not set"
        log.info("Primitive default 0L — ambiguous: new entity or id=0? "
                + primitiveId);

        // Output:
        // Before save — id: null, persisted: false
        // After save  — id: 42, persisted: true
        // Primitive default 0L — ambiguous: new entity or id=0? 0
    }
}