// Java 8+
/**
 * Listing 2.3 — LambdaPredicate.java
 * Demonstrates: Replacing anonymous class ceremony with lambda expressions
 * Chapter 2: Writing Code the Modern Java Way
 * Requires: Java 8+
 */
package chapter02;

import java.util.function.Predicate;

public class LambdaPredicate {

    public static void main(String[] args) {

        // Step 1: remove anonymous class boilerplate — still uses explicit braces/return
        Predicate<Integer> isEven = (number) -> {
            return number % 2 == 0; // compiler infers Integer param and boolean return
        };

        // Step 2: single param — parentheses optional; single expression — no braces or return
        Predicate<Integer> isEvenLambda = number -> number % 2 == 0;

        // Demonstrate both predicates produce identical results
        System.out.println("isEven.test(4):       " + isEven.test(4));       // true
        System.out.println("isEven.test(7):       " + isEven.test(7));       // false
        System.out.println("isEvenLambda.test(4): " + isEvenLambda.test(4)); // true
        System.out.println("isEvenLambda.test(7): " + isEvenLambda.test(7)); // false

        // Compose predicates: negate isEvenLambda to get isOdd
        Predicate<Integer> isOdd = isEvenLambda.negate(); // built-in default method
        System.out.println("isOdd.test(7):        " + isOdd.test(7));        // true
    }

    // Output:
    // isEven.test(4):       true
    // isEven.test(7):       false
    // isEvenLambda.test(4): true
    // isEvenLambda.test(7): false
    // isOdd.test(7):        true
}