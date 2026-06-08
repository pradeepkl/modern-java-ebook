// Java 8+
/**
 * Listing 2.5 — HigherOrderFunctions.java
 * Demonstrates: Composing and combining Predicate instances using
 *               or(), negate(), and and() for higher-order function patterns.
 * Chapter 2: Writing Code the Modern Java Way
 * Requires: Java 8+
 */
package chapter02;

import java.util.function.Predicate;

public class HigherOrderFunctions {

    public static void main(String[] args) {

        // Define two base predicates as lambda expressions
        Predicate<Integer> isMinor  = age -> age < 18;  // under 18
        Predicate<Integer> isSenior = age -> age > 65;  // over 65

        // Combine predicates using logical OR
        Predicate<Integer> isMinorOrSenior = isMinor.or(isSenior);
        System.out.println(isMinorOrSenior.test(16)); // true  — minor
        System.out.println(isMinorOrSenior.test(30)); // false — neither

        // Negate a predicate: isNotMinor means age >= 18
        Predicate<Integer> isNotMinor = isMinor.negate();
        System.out.println(isNotMinor.test(20));      // true  — not a minor
        System.out.println(isNotMinor.test(15));      // false — is a minor

        // Compose: adult is NOT minor AND NOT senior (18–65 inclusive)
        Predicate<Integer> isBetween18And65 =
                isMinor.negate().and(isSenior.negate());
        System.out.println(isBetween18And65.test(30)); // true  — working age
        System.out.println(isBetween18And65.test(70)); // false — senior

        // Output:
        // true
        // false
        // true
        // false
        // true
        // false
    }
}