// Java 25+
// Feature shown: records, final in Java 16+

/**
 * Listing 3.11 — PersonRecord.java
 * Demonstrates: records as immutable data carriers with custom instance methods
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 16+ for records; Java 25+ for compact source files and
 * instance main methods (void main()), compiled with --enable-preview --release 21
 */
package chapter03;

import java.util.logging.Logger;

public record PersonRecord(String name, int age) {

    // Logger for output — records can declare static fields
    private static final Logger LOG = Logger.getLogger(PersonRecord.class.getName());

    // Custom instance method added to the record
    public String greet() {
        return "Hello, my name is " + name
                + " and I am " + age + " years old.";
    }

    // Instance main method — compact source file style (Java 25+, JEP 512)
    void main() {
        // Create an immutable record instance
        PersonRecord person = new PersonRecord("Alice", 30);

        // Access auto-generated accessor methods
        LOG.info("Name: " + person.name());
        LOG.info("Age: " + person.age());

        // Invoke the custom greet method
        LOG.info(person.greet());

        // Records provide auto-generated equals, hashCode, and toString
        PersonRecord duplicate = new PersonRecord("Alice", 30);
        LOG.info("Equals: " + person.equals(duplicate));
        LOG.info("toString: " + person);

        // Output:
        // Name: Alice
        // Age: 30
        // Hello, my name is Alice and I am 30 years old.
        // Equals: true
        // toString: PersonRecord[name=Alice, age=30]
    }
}