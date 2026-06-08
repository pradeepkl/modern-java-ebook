// Java 8+
/**
 * Listing 2.5 — HigherOrderFunctions.java
 * Demonstrates: Composing predicates using higher-order functions (and, negate, or)
 * Chapter 2: Expressing Intent with Modern Java
 * Requires: Java 8+
 */
package chapter02;

import java.util.function.Predicate;

public class HigherOrderFunctions {

    public static void main(String[] args) {

        // Define individual predicates as reusable behaviors
        Predicate<Integer> isEven = number -> number % 2 == 0;
        Predicate<Integer> isPositive = number -> number > 0;

        // Compose predicates: even AND positive — both conditions must hold
        Predicate<Integer> isEvenAndPositive = isEven.and(isPositive);

        System.out.println(isEvenAndPositive.test(4));   // true: even and positive
        System.out.println(isEvenAndPositive.test(-4));  // false: even but not positive
        System.out.println(isEvenAndPositive.test(3));   // false: positive but not even

        // Negate to get odd numbers — logical complement of isEven
        Predicate<Integer> isOdd = isEven.negate();
        System.out.println(isOdd.test(3));   // true: 3 is odd
        System.out.println(isOdd.test(4));   // false: 4 is even

        // Compose with OR: even OR positive
        Predicate<Integer> isEvenOrPositive = isEven.or(isPositive);
        System.out.println(isEvenOrPositive.test(-3));  // false: odd and negative
        System.out.println(isEvenOrPositive.test(-4));  // true: even (though negative)

        // Output:
        // true
        // false
        // false
        // true
        // false
        // false
        // true
    }
}