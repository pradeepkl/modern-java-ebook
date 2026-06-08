// Java 16+
/**
 * Listing 3.12 — EmailAddressRecord.java
 * Demonstrates: Records implementing multiple interfaces for composed behavior
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 16+
 */
package chapter03;

/**
 * Capability interface: validation contract.
 */
interface Validatable {
    boolean isValid();
}

/**
 * Capability interface: display contract.
 */
interface Displayable {
    String display();
}

/**
 * Immutable data carrier implementing two distinct behavioral interfaces.
 * No inheritance hierarchy — behavior is composed, not inherited.
 */
record EmailAddress(String value) implements Validatable, Displayable {

    @Override
    public boolean isValid() {
        return value != null && value.contains("@"); // minimal validation rule
    }

    @Override
    public String display() {
        return value.toLowerCase(); // normalized display form
    }

    // Derived computed view — no state mutation
    public String domain() {
        return value.substring(value.indexOf('@') + 1); // extract domain portion
    }
}

public class EmailAddressRecord {

    public static void main(String[] args) {
        EmailAddress valid = new EmailAddress("User@Example.COM");
        EmailAddress invalid = new EmailAddress("not-an-email");

        // Demonstrate Validatable interface
        System.out.println("Valid?   " + valid.isValid());    // true
        System.out.println("Valid?   " + invalid.isValid()); // false

        // Demonstrate Displayable interface
        System.out.println("Display: " + valid.display());   // user@example.com

        // Demonstrate derived computed accessor
        System.out.println("Domain:  " + valid.domain());    // example.com

        // Records provide equals/hashCode/toString automatically
        System.out.println("Record:  " + valid);             // EmailAddress[value=User@Example.COM]

        // Output:
        // Valid?   true
        // Valid?   false
        // Display: user@example.com
        // Domain:  example.com
        // Record:  EmailAddress[value=User@Example.COM]
    }
}