// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 13.9 — FluentTransformations.java
 * Demonstrates: Declarative list transformation using removeIf, replaceAll, sort, forEach
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter13;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class FluentTransformations {

    private static final Logger log = Logger.getLogger(FluentTransformations.class.getName());

    record Product(String productId, String category, double price, boolean available) {}

    void main() {
        // Mutable catalogue for in-place transformation
        List<Product> catalogue = new ArrayList<>(List.of(
            new Product("A1", "Books",       12.00, true),
            new Product("B2", "Electronics", 99.99, false),
            new Product("C3", "Books",        8.50, true),
            new Product("D4", "Electronics", 45.00, true),
            new Product("E5", "Clothing",    25.00, false),
            new Product("F6", "Clothing",    18.00, true)
        ));

        // removeIf — one sentence: remove unavailable products
        catalogue.removeIf(p -> !p.available());

        // replaceAll — one sentence: apply 10 percent discount to every product
        catalogue.replaceAll(p ->
            new Product(p.productId(), p.category(),
                        p.price() * 0.9, p.available()));

        // sort — one sentence: order by category then by price
        catalogue.sort(
            Comparator.comparing(Product::category)
                      .thenComparingDouble(Product::price));

        // forEach — one sentence: log each product
        catalogue.forEach(p ->
            log.info(p.category()
                + " | " + p.productId()
                + " | price=" + String.format("%.2f", p.price())));

        // Output:
        // Books       | C3 | price=7.65
        // Books       | A1 | price=10.80
        // Clothing    | F6 | price=16.20
        // Electronics | D4 | price=40.50
    }
}