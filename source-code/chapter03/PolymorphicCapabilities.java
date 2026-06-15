// Java 25+
// Feature shown: records, final in Java 16+

/**
 * Listing 3.13 — PolymorphicCapabilities.java
 * Demonstrates: composing polymorphic behavior through interfaces on a record,
 *               showing that a single instance can satisfy multiple interface types
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter03;

import java.util.logging.Logger;

interface Validatable {
    boolean isValid();
}

interface Displayable {
    String display();
}

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

    private static final Logger LOG =
            Logger.getLogger(PolymorphicCapabilities.class.getName());

    // Accepts the record through two distinct interface lenses
    void printIfValid(Validatable v, Displayable d) {
        if (v.isValid()) {
            LOG.info(d.display()); // only display when valid
        }
    }

    void main() {
        EmailAddress email = new EmailAddress("Alice@Example.com");

        printIfValid(email, email);       // same instance satisfies both interfaces
        LOG.info(email.domain());         // derived view from record component

        // Output:
        // alice@example.com
        // Example.com
    }
}