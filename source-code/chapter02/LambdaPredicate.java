// Java 8+
/**
 * Listing 2.3 — LambdaPredicate.java
 * Demonstrates: Replacing anonymous class ceremony with lambda expressions
 * Chapter 2: Expressing Intent with Modern Java
 * Requires: Java 8+
 */
package chapter02;

import java.util.function.Predicate;

public class LambdaPredicate {

    public static void main(String[] args) {

        // Step 1: Anonymous class — verbose but explicit
        Predicate<Integer> isEvenAnon = new Predicate<Integer>() {
            @Override
            public boolean test(Integer number) {
                return number % 2 == 0; // single abstract method of Predicate<T>
            }
        };

        // Step 2: Lambda with braces — class body, method name, @Override removed
        Predicate<Integer> isEven = (number) -> {
            return number % 2 == 0;
        };

        // Step 3: Compact lambda — parentheses, braces, and return keyword removed
        Predicate<Integer> isEvenLambda = number -> number % 2 == 0;

        // Demonstrate all three forms produce identical results
        System.out.println("Anonymous class  isEven.test(4): " + isEvenAnon.test(4));   // true
        System.out.println("Lambda with body isEven.test(4): " + isEven.test(4));       // true
        System.out.println("Compact lambda   isEven.test(4): " + isEvenLambda.test(4)); // true

        System.out.println("Anonymous class  isEven.test(7): " + isEvenAnon.test(7));   // false
        System.out.println("Lambda with body isEven.test(7): " + isEven.test(7));       // false
        System.out.println("Compact lambda   isEven.test(7): " + isEvenLambda.test(7)); // false

        // Output:
        // Anonymous class  isEven.test(4): true
        // Lambda with body isEven.test(4): true
        // Compact lambda   isEven.test(4): true
        // Anonymous class  isEven.test(7): false
        // Lambda with body isEven.test(7): false
        // Compact lambda   isEven.test(7): false
    }
}