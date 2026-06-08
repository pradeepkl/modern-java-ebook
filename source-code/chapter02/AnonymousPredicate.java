// Java 8+
/**
 * Listing 2.2 — AnonymousPredicate.java
 * Demonstrates: Anonymous class implementation of a functional interface (Predicate)
 * Chapter 2: Writing Code the Modern Java Way
 * Requires: Java 8+
 */
package chapter02;

import java.util.function.Predicate;

public class AnonymousPredicate {

    public static void main(String[] args) {

        // Anonymous class implementation of Predicate — verbose but explicit
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean test(Integer number) {
                return number % 2 == 0; // single abstract method of Predicate<T>
            }
        };

        // Test with an even number
        System.out.println("Is 4 even? " + isEven.test(4));

        // Test with an odd number
        System.out.println("Is 7 even? " + isEven.test(7));

        // Demonstrate negation using the default negate() method
        Predicate<Integer> isOdd = isEven.negate(); // default method on Predicate
        System.out.println("Is 3 odd?  " + isOdd.test(3));

        // Output:
        // Is 4 even? true
        // Is 7 even? false
        // Is 3 odd?  true
    }
}