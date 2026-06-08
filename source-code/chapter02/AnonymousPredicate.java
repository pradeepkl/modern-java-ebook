// Java 8+
/**
 * Listing 2.2 — AnonymousPredicate.java
 * Demonstrates: Implementing a functional interface using an anonymous class
 * Chapter 2: Expressing Intent with Modern Java
 * Requires: Java 8+
 */
package chapter02;

import java.util.function.Predicate;

public class AnonymousPredicate {

    public static void main(String[] args) {

        // Anonymous class implementation — verbose but explicit
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean test(Integer number) {
                return number % 2 == 0; // single abstract method of Predicate<T>
            }
        };

        // Test with an even number — expects true
        System.out.println(isEven.test(4));

        // Test with an odd number — expects false
        System.out.println(isEven.test(7));

        // Demonstrate negation using Predicate.negate()
        Predicate<Integer> isOdd = isEven.negate(); // composed from existing predicate
        System.out.println(isOdd.test(3));  // true — 3 is odd
        System.out.println(isOdd.test(8));  // false — 8 is even
    }

    // Output:
    // true
    // false
    // true
    // false
}