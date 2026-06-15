// Java 25+
// Feature shown: records implementing interfaces, final in Java 16+

/**
 * Listing 3.12 — EmailAddressRecord.java
 * Demonstrates: a record implementing multiple interfaces for composable behavior
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 16+ for records; void main() instance main method final in Java 25+
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
        return value != null && value.contains("@"); // minimal validation
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

public class EmailAddressRecord {

    private static final Logger LOG = Logger.getLogger(EmailAddressRecord.class.getName());

    void main() {
        EmailAddress valid = new EmailAddress("User@Example.COM");
        EmailAddress invalid = new EmailAddress("notanemail");

        LOG.info("Valid address: " + valid.display());         // user@example.com
        LOG.info("Is valid: " + valid.isValid());              // true
        LOG.info("Domain: " + valid.domain());                 // Example.COM

        LOG.info("Invalid address: " + invalid.display());     // notanemail
        LOG.info("Is valid: " + invalid.isValid());            // false

        LOG.info("toString: " + valid);                        // EmailAddress[value=User@Example.COM]
        LOG.info("Equals same value: " + valid.equals(new EmailAddress("User@Example.COM"))); // true

        // Output:
        // Valid address: user@example.com
        // Is valid: true
        // Domain: Example.COM
        // Invalid address: notanemail
        // Is valid: false
        // toString: EmailAddress[value=User@Example.COM]
        // Equals same value: true
    }
}