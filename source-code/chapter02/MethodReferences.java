// Java 8+
/**
 * Listing 2.7 — MethodReferences.java
 * Demonstrates: Four types of method references and constructor references
 * Chapter 2: Writing Code the Modern Java Way
 * Requires: Java 8+
 */
package chapter02;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class MethodReferences {

    public static void main(String[] args) {

        // Traditional anonymous class — verbose but explicit
        Function<String, Integer> parseViaAnon = new Function<String, Integer>() {
            @Override
            public Integer apply(String str) {
                return Integer.parseInt(str);
            }
        };

        // Lambda expression — more concise
        Function<String, Integer> parseViaLambda = str -> Integer.parseInt(str);

        // Method reference to static method — cleanest form
        Function<String, Integer> parseViaRef = Integer::parseInt;

        System.out.println("Anonymous:  " + parseViaAnon.apply("42"));
        System.out.println("Lambda:     " + parseViaLambda.apply("42"));
        System.out.println("MethodRef:  " + parseViaRef.apply("42"));

        // Reference to an instance method of a particular object
        String greeting = "Hello, World!";
        Supplier<String> toUpperCase = greeting::toUpperCase; // bound to 'greeting'
        System.out.println("UpperCase:  " + toUpperCase.get());

        // Reference to an instance method of an arbitrary object of a type
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        names.sort(String::compareToIgnoreCase); // (s1, s2) -> s1.compareToIgnoreCase(s2)
        System.out.println("Sorted:     " + names);

        // Reference to a constructor — deferred instantiation
        Supplier<List<String>> listSupplier = ArrayList::new; // () -> new ArrayList<>()
        List<String> newList = listSupplier.get();
        System.out.println("NewList:    " + newList);

        // Output: Anonymous:  42
        //         Lambda:     42
        //         MethodRef:  42
        //         UpperCase:  HELLO, WORLD!
        //         Sorted:     [Alice, Bob, Charlie]
        //         NewList:    []
    }
}