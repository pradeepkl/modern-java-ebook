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

        // Test with an even number
        System.out.println("Is 4 even? " + isEven.test(4));

        // Test with an odd number
        System.out.println("Is 7 even? " + isEven.test(7));

        // Demonstrate reuse with additional values
        System.out.println("Is 10 even? " + isEven.test(10));
        System.out.println("Is 13 even? " + isEven.test(13));
    }

    // Output:
    // Is 4 even? true
    // Is 7 even? false
    // Is 10 even? true
    // Is 13 even? false
}