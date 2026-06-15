// Java 25+
// Feature shown: method references, final in Java 8+
package chapter02;

import java.util.List;
import java.util.logging.Logger;

/**
 * Listing 2.5 — MethodReferences.java
 * Demonstrates: method references as concise alternatives to single-method lambdas
 * Chapter 2: Writing Java the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
public class MethodReferences {

    private static final Logger log =
            Logger.getLogger(MethodReferences.class.getName());

    // Simple record used as the data type in the stream pipeline
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

        // Both pipelines produce identical results
        log.info("Lambda:    " + lambdaNames);
        log.info("Reference: " + refNames);

        // Instance method reference on a particular object
        String prefix = "Item: ";
        var prefixed = products.stream()
                .map(p -> prefix.concat(p.name()))  // lambda form
                .toList();
        log.info("Prefixed:  " + prefixed);

        // Static method reference — Double::parseDouble as a transformer
        var priceStrings = List.of("9.99", "19.99", "4.49");
        var prices = priceStrings.stream()
                .map(Double::parseDouble)   // reference to static method
                .toList();
        log.info("Prices:    " + prices);

        // Output:
        // Lambda:    [Keyboard, Monitor, Mouse]
        // Reference: [Keyboard, Monitor, Mouse]
        // Prefixed:  [Item: Keyboard, Item: Monitor, Item: Mouse]
        // Prices:    [9.99, 19.99, 4.49]
    }
}