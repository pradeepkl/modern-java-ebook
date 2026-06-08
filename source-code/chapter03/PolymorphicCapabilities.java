// Java 16+
/**
 * Listing 3.13 — PolymorphicCapabilities.java
 * Demonstrates: Polymorphic use of records implementing multiple interfaces
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 16+
 */
package chapter03;

// Interface representing validation capability
interface Validatable {
    boolean isValid();
}

// Interface representing display capability
interface Displayable {
    String display();
}

// Record implementing both interfaces — behavior composed, not inherited
record EmailAddress(String value) implements Validatable, Displayable {

    @Override
    public boolean isValid() {
        return value != null && value.contains("@"); // basic validation
    }

    @Override
    public String display() {
        return value.toLowerCase(); // normalized display form
    }

    // Derived computed view — no state mutation
    public String domain() {
        return value.substring(value.indexOf('@') + 1);
    }
}

public class PolymorphicCapabilities {

    // Accepts the record through two separate interface lenses
    static void printIfValid(Validatable v, Displayable d) {
        if (v.isValid()) {
            System.out.println(d.display()); // only display if valid
        }
    }

    public static void main(String[] args) {
        // Single record instance satisfies both interface contracts
        EmailAddress email = new EmailAddress("Alice@Example.com");

        // Pass same instance as both Validatable and Displayable
        printIfValid(email, email);

        // Derived view computed from immutable state
        System.out.println(email.domain());

        // Demonstrate invalid address is silently skipped
        EmailAddress invalid = new EmailAddress("notanemail");
        printIfValid(invalid, invalid); // prints nothing

        // Output: alice@example.com
        //         example.com
    }
}