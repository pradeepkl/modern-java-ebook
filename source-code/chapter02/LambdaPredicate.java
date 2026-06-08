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

        // Step 1: remove anonymous class ceremony — block lambda with explicit return
        Predicate<Integer> isEven = (number) -> {
            return number % 2 == 0; // compiler infers Integer type and boolean return
        };

        // Step 2: parentheses optional for single param; braces and return removed
        Predicate<Integer> isEvenLambda = number -> number % 2 == 0; // most concise form

        // Step 3: compose predicates — negate isEven to get isOdd
        Predicate<Integer> isOdd = isEvenLambda.negate(); // built-in composition method

        System.out.println("isEven.test(4):      " + isEven.test(4));       // true
        System.out.println("isEvenLambda.test(7):" + isEvenLambda.test(7)); // false
        System.out.println("isOdd.test(7):       " + isOdd.test(7));        // true
        System.out.println("isOdd.test(4):       " + isOdd.test(4));        // false

        // Output:
        // isEven.test(4):      true
        // isEvenLambda.test(7):false
        // isOdd.test(7):       true
        // isOdd.test(4):       false
    }
}