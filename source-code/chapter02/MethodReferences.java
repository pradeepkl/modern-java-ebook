// Java 8+
/**
 * Listing 2.7 — MethodReferences.java
 * Demonstrates: Four types of method references in Java
 * Chapter 2: Expressing Intent with Modern Java
 * Requires: Java 8+
 */
package chapter02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class MethodReferences {

    public static void main(String[] args) {

        // Static method reference replaces explicit lambda: s -> Integer.parseInt(s)
        Function<String, Integer> parseViaRef = Integer::parseInt;
        System.out.println(parseViaRef.apply("42")); // 42

        // Instance method reference on a specific object: () -> greeting.toUpperCase()
        String greeting = "Hello, World!";
        Supplier<String> toUpperCase = greeting::toUpperCase;
        System.out.println(toUpperCase.get()); // HELLO, WORLD!

        // Instance method reference on an arbitrary object of a type
        // Equivalent to the lambda: (a, b) -> a.compareToIgnoreCase(b)
        List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
        names.sort(String::compareToIgnoreCase);
        System.out.println(names); // [Alice, Bob, Charlie]

        // Constructor reference to create a new instance: () -> new ArrayList<>()
        Supplier<List<String>> listSupplier = ArrayList::new;
        List<String> newList = listSupplier.get();
        newList.add("Java");
        System.out.println(newList); // [Java]
    }

    // Output: 42
    //         HELLO, WORLD!
    //         [Alice, Bob, Charlie]
    //         [Java]
}