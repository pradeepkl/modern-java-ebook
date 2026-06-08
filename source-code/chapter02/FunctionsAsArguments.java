// Java 8+
/**
 * Listing 2.4 — FunctionsAsArguments.java
 * Demonstrates: Passing and returning lambdas as first-class behavior
 * Chapter 2: Expressing Intent with Modern Java
 * Requires: Java 8+
 */
package chapter02;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FunctionsAsArguments {

    /**
     * Accepts behavior as an argument via a functional interface.
     * The caller decides the filtering logic at the call site.
     */
    public static List<Integer> filterNumbers(List<Integer> numbers, Predicate<Integer> condition) {
        List<Integer> result = new ArrayList<>();
        for (Integer number : numbers) {
            if (condition.test(number)) result.add(number); // delegate decision to caller
        }
        return result;
    }

    /**
     * Returns behavior (a lambda) from a method — a higher-order function.
     * Captures the threshold value via closure.
     */
    public static Predicate<Integer> isGreaterThan(int threshold) {
        return number -> number > threshold; // threshold captured from enclosing scope
    }

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 5, 10, 15, 20);

        // Pass a lambda directly as an argument
        List<Integer> evens = filterNumbers(numbers, n -> n % 2 == 0);
        System.out.println("Even numbers:        " + evens);

        // Pass a returned lambda (closure) as an argument
        List<Integer> greaterThan9 = filterNumbers(numbers, isGreaterThan(9));
        System.out.println("Greater than 9:      " + greaterThan9);

        // Compose predicates: greater than 4 AND even
        Predicate<Integer> greaterThan4AndEven = isGreaterThan(4).and(n -> n % 2 == 0);
        List<Integer> combined = filterNumbers(numbers, greaterThan4AndEven);
        System.out.println("Greater than 4 & even: " + combined);

        // Output: Even numbers:          [10, 20]
        // Output: Greater than 9:        [10, 15, 20]
        // Output: Greater than 4 & even: [10, 20]
    }
}