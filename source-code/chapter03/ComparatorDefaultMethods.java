// Java 8+
/**
 * Listing 3.6 — ComparatorDefaultMethods.java
 * Demonstrates: Comparator default methods (thenComparing) and Iterable forEach
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 8+
 */
package chapter03;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ComparatorDefaultMethods {

    public static void main(String[] args) {

        List<String> names = Arrays.asList("John", "Alice", "Bob", "Alice");

        // thenComparing is a default method on Comparator (Java 8+)
        // Sorts by name length first, then alphabetically for equal lengths
        names.sort(
            Comparator.comparingInt(String::length)   // primary: sort by length
                      .thenComparing(Comparator.naturalOrder()) // secondary: alphabetical
        );

        // Prints sorted list: shortest first, ties broken alphabetically
        System.out.println(names);
        // Output: [Bob, John, Alice, Alice]

        // forEach is a default method on Iterable (Java 8+)
        // filter() and forEach() are default/intermediate methods on Stream
        names.stream()
             .filter(n -> n.length() > 3)   // keep names longer than 3 chars
             .forEach(System.out::println);  // method reference as Consumer
        // Output:
        // John
        // Alice
        // Alice

        // Demonstrating reversed() — another Comparator default method
        System.out.println("\nReversed order:");
        names.stream()
             .sorted(Comparator.comparingInt(String::length)
                               .thenComparing(Comparator.naturalOrder())
                               .reversed()) // reversed() is also a default method
             .forEach(System.out::println);
        // Output:
        // Alice
        // Alice
        // John
        // Bob
    }
}