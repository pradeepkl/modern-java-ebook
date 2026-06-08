// Java 10+
package chapter02;

/**
 * Listing 2.11 — VarargsStandardLibrary.java
 * Demonstrates: Varargs usage in Java standard library APIs
 * Chapter 2: Writing Code the Modern Java Way
 * Requires: Java 10+
 */

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class VarargsStandardLibrary {

    public static void main(String[] args) {
        // String.format accepts a format string plus any number of arguments
        var formatted = String.format("Name: %s, Score: %d", "Alice", 95);

        // List.of() accepts varargs — no explicit array needed
        var list = List.of("Java", "is", "expressive");

        // Stream.of() wraps varargs into a stream pipeline seamlessly
        var stream = Stream.of(10, 20, 30, 40);

        // Arrays.asList() also uses varargs for convenient list creation
        var legacy = Arrays.asList("one", "two", "three");

        System.out.println(formatted);
        System.out.println(list);

        // Each element doubled via stream pipeline
        stream.map(n -> n * 2).forEach(System.out::println);

        System.out.println(legacy);
    }

    // Output:
    // Name: Alice, Score: 95
    // [Java, is, expressive]
    // 20
    // 40
    // 60
    // 80
    // [one, two, three]
}