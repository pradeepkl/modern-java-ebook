// Java 25+
// Feature shown: lambda expressions, final in Java 8+
/**
 * Listing 2.3 — LambdaPredicate.java
 * Demonstrates: lambda expressions vs anonymous class implementations of Predicate
 * Chapter 2: Writing Java the Modern Way
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter02;

import java.util.function.Predicate;
import java.util.logging.Logger;

public class LambdaPredicate {

    private static final Logger log =
            Logger.getLogger(LambdaPredicate.class.getName());

    void main() {
        // Anonymous class — ceremony for a single line of logic
        Predicate<Integer> isEvenAnon =
                new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer n) {
                        return n % 2 == 0; // single abstract method implemented verbosely
                    }
                };

        // Lambda — same behavior, intent visible immediately
        // Compiler infers: input type Integer, return type boolean, method is test()
        Predicate<Integer> isEven = n -> n % 2 == 0;

        // Both predicates produce identical results
        log.info("Anonymous: " + isEvenAnon.test(4)); // true
        log.info("Lambda:    " + isEven.test(4));     // true

        log.info("Anonymous: " + isEvenAnon.test(7)); // false
        log.info("Lambda:    " + isEven.test(7));     // false

        // Output:
        // INFO: Anonymous: true
        // INFO: Lambda:    true
        // INFO: Anonymous: false
        // INFO: Lambda:    false
    }
}