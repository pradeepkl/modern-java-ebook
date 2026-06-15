// Java 25+
// Feature shown: method references, final in Java 8+
package chapter02;

import java.util.List;
import java.util.logging.Logger;

/**
 * Listing 2.5 — MethodReferences.java
 * Demonstrates: method references as concise alternatives to lambdas
 * Chapter 2: Writing Java the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
public class MethodReferences {

    private static final Logger log =
            Logger.getLogger(MethodReferences.class.getName());

    // Record used as the data type for stream operations
    record Product(String name, double price) {}

    void main() {
        var products = List.of(
                new Product("Keyboard", 79.99),
                new Product("Monitor", 349.99),
                new Product("Mouse", 29.99));

        // Lambda — explicit parameter and method call
        var lambdaNames = products.stream()
                .map(p -> p.name())   // p is named, call is explicit
                .toList();

        // Method reference — same intent, less ceremony
        var refNames = products.stream()
                .map(Product::name)   // reads as "the name of a Product"
                .toList();

        // Lambda — explicit parameter for price extraction
        var lambdaPrices = products.stream()
                .map(p -> p.price())  // verbose but clear
                .toList();

        // Method reference — instance method of an arbitrary object
        var refPrices = products.stream()
                .map(Product::price)  // concise, same semantics
                .toList();

        log.info("Lambda names:    " + lambdaNames);
        log.info("Reference names: " + refNames);
        log.info("Lambda prices:    " + lambdaPrices);
        log.info("Reference prices: " + refPrices);

        // Output:
        // Lambda names:    [Keyboard, Monitor, Mouse]
        // Reference names: [Keyboard, Monitor, Mouse]
        // Lambda prices:    [79.99, 349.99, 29.99]
        // Reference prices: [79.99, 349.99, 29.99]
    }
}