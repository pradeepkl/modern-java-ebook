// Java 8+
/**
 * Listing 2.5 — HigherOrderFunctions.java
 * Demonstrates: Composing and negating Predicate instances as higher-order functions
 * Chapter 2: Expressing Intent with Modern Java
 * Requires: Java 8+
 */
package chapter02;

import java.util.function.Predicate;

public class HigherOrderFunctions {

    public static void main(String[] args) {

        // Assign behavior to variables using lambda expressions
        Predicate<Integer> isEven = number -> number % 2 == 0;
        Predicate<Integer> isPositive = number -> number > 0;

        // Compose predicates: even AND positive (higher-order combination)
        Predicate<Integer> isEvenAndPositive = isEven.and(isPositive);

        // 4 is even and positive → true
        System.out.println(isEvenAndPositive.test(4));

        // -4 is even but not positive → false
        System.out.println(isEvenAndPositive.test(-4));

        // Negate the isEven predicate to produce an isOdd predicate
        Predicate<Integer> isOdd = isEven.negate();

        // 3 is odd → true
        System.out.println(isOdd.test(3));

        // Demonstrate or() composition: even OR positive
        Predicate<Integer> isEvenOrPositive = isEven.or(isPositive);

        // -3 is odd and negative → false
        System.out.println(isEvenOrPositive.test(-3));

        // -2 is even (but negative) → true
        System.out.println(isEvenOrPositive.test(-2));

        // Output: true
        //         false
        //         true
        //         false
        //         true
    }
}