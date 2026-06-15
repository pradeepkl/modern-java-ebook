// Java 25+
// Feature shown: records, final in Java 16+

/**
 * Listing 3.11 — PersonRecord.java
 * Demonstrates: records as immutable data carriers with custom instance methods
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter03;

import java.util.logging.Logger;

public record PersonRecord(String name, int age) {

    // Custom compact constructor for validation
    public PersonRecord {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Age must not be negative");
        }
    }

    // Custom instance method added to the record
    public String greet() {
        return "Hello, my name is " + name
                + " and I am " + age + " years old.";
    }

    void main() {
        Logger log = Logger.getLogger(PersonRecord.class.getName());

        // Records provide equals, hashCode, and toString automatically
        PersonRecord alice = new PersonRecord("Alice", 30);
        PersonRecord bob   = new PersonRecord("Bob", 25);
        PersonRecord alice2 = new PersonRecord("Alice", 30);

        log.info(alice.greet());                              // custom method
        log.info(bob.greet());

        log.info("alice.name()  : " + alice.name());         // accessor
        log.info("alice.age()   : " + alice.age());          // accessor
        log.info("alice.toString: " + alice);                 // auto toString
        log.info("alice == alice2: " + alice.equals(alice2)); // value equality

        // Output:
        // Hello, my name is Alice and I am 30 years old.
        // Hello, my name is Bob and I am 25 years old.
        // alice.name()  : Alice
        // alice.age()   : 30
        // alice.toString: PersonRecord[name=Alice, age=30]
        // alice == alice2: true
    }
}