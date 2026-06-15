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
                        return n % 2 == 0; // explicit method body required
                    }
                };

        // Lambda — compiler infers method name and return type from Predicate<T>
        Predicate<Integer> isEven = n -> n % 2 == 0;

        // Both predicates produce identical results
        log.info("Anonymous test(4): " + isEvenAnon.test(4)); // true
        log.info("Lambda    test(4): " + isEven.test(4));     // true
        log.info("Anonymous test(7): " + isEvenAnon.test(7)); // false
        log.info("Lambda    test(7): " + isEven.test(7));     // false

        // Lambdas compose naturally using Predicate combinators
        Predicate<Integer> isOdd = isEven.negate(); // logical complement
        log.info("isOdd     test(7): " + isOdd.test(7));      // true

        // Output:
        // Anonymous test(4): true
        // Lambda    test(4): true
        // Anonymous test(7): false
        // Lambda    test(7): false
        // isOdd     test(7): true
    }
}