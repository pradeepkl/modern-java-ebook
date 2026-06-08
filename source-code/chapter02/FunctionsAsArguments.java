// Java 8+
/**
 * Listing 2.4 — FunctionsAsArguments.java
 * Demonstrates: Passing and returning behavior using lambdas and Predicate
 * Chapter 2: Writing Code the Modern Java Way
 * Requires: Java 8+
 */
package chapter02;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FunctionsAsArguments {

    // Accepts behavior as an argument via the Predicate functional interface
    public static List<Integer> filterNumbers(
            List<Integer> numbers,
            Predicate<Integer> condition) {

        List<Integer> result = new ArrayList<>();

        for (Integer number : numbers) {
            if (condition.test(number)) { // invoke the passed-in behavior
                result.add(number);
            }
        }

        return result;
    }

    // Returns behavior (a lambda) from a method — higher-order function
    public static Predicate<Integer> isGreaterThan(int threshold) {
        return number -> number > threshold; // captures threshold via closure
    }

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 5, 10, 15, 20);

        // Pass a lambda directly as the condition argument
        List<Integer> evens = filterNumbers(numbers, n -> n % 2 == 0);
        System.out.println("Even numbers:        " + evens);

        // Use a method that returns a Predicate (behavior factory)
        Predicate<Integer> greaterThanNine = isGreaterThan(9);
        List<Integer> bigNumbers = filterNumbers(numbers, greaterThanNine);
        System.out.println("Greater than 9:      " + bigNumbers);

        // Compose predicates: greater than 4 AND even
        List<Integer> evenAndBig = filterNumbers(numbers, isGreaterThan(4).and(n -> n % 2 == 0));
        System.out.println("Even and > 4:        " + evenAndBig);

        // Output:
        // Even numbers:        [10, 20]
        // Greater than 9:      [10, 15, 20]
        // Even and > 4:        [10, 20]
    }
}