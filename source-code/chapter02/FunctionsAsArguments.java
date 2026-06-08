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

    // Accepts behavior as an argument via functional interface
    public static List<Integer> filterNumbers(List<Integer> numbers, Predicate<Integer> condition) {
        List<Integer> result = new ArrayList<>();
        for (Integer number : numbers) {
            if (condition.test(number)) result.add(number); // apply the passed-in behavior
        }
        return result;
    }

    // Returns behavior (a lambda) from a method — higher-order function
    public static Predicate<Integer> isGreaterThan(int threshold) {
        return number -> number > threshold; // captures threshold via closure
    }

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 5, 10, 15, 20);

        // Pass a lambda directly as an argument
        List<Integer> evens = filterNumbers(numbers, n -> n % 2 == 0);
        System.out.println("Even numbers: " + evens);

        // Pass a method-returned lambda as an argument
        List<Integer> greaterThanNine = filterNumbers(numbers, isGreaterThan(9));
        System.out.println("Greater than 9: " + greaterThanNine);

        // Compose predicates: greater than 4 AND even
        Predicate<Integer> greaterThanFour = isGreaterThan(4);
        Predicate<Integer> isEven = n -> n % 2 == 0;
        List<Integer> evenAndGreaterThanFour = filterNumbers(numbers, greaterThanFour.and(isEven));
        System.out.println("Even and greater than 4: " + evenAndGreaterThanFour);

        // Output: Even numbers: [10, 20]
        // Output: Greater than 9: [10, 15, 20]
        // Output: Even and greater than 4: [10, 20]
    }
}