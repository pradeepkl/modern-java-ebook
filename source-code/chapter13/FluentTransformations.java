// Java 8+
/**
 * Listing 13.9 — FluentTransformations.java
 * Demonstrates: Imperative vs declarative transformation style using
 *               removeIf, replaceAll, sort, and forEach on a List
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 8+
 */
package chapter13;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class FluentTransformations {

    private static final Logger log = Logger.getLogger(FluentTransformations.class.getName());

    record Product(String productId, String category, double price, boolean available) {}

    public static void main(String[] args) {

        // --- Imperative style: mechanics dominate intent ---
        List<Product> catalogue = buildCatalogue();
        List<Product> result = new ArrayList<>();
        for (Product p : catalogue) {
            if (p.available()) {
                result.add(new Product(p.productId(),
                        p.category(), p.price() * 0.9,
                        p.available()));
            }
        }
        result.sort((a, b) -> Double.compare(a.price(), b.price()));
        log.info("=== Imperative result ===");
        result.forEach(p -> log.info(p.category() + " | " + p.productId() + " | £" + p.price()));

        // --- Declarative style: each line is one sentence ---
        List<Product> catalogue2 = buildCatalogue(); // fresh mutable list

        // Remove unavailable products — reads as intent
        catalogue2.removeIf(p -> !p.available());

        // Apply 10% discount to every remaining product
        catalogue2.replaceAll(p ->
                new Product(p.productId(), p.category(),
                        p.price() * 0.9, p.available()));

        // Sort by category, then by price within each category
        catalogue2.sort(
                Comparator.comparing(Product::category)
                        .thenComparingDouble(Product::price));

        log.info("=== Declarative result ===");
        catalogue2.forEach(p ->
                log.info(p.category()
                        + " | " + p.productId()
                        + " | £" + p.price()));

        // Output:
        // === Imperative result ===
        // Books | B001 | £13.5
        // Electronics | E001 | £45.0
        // Electronics | E002 | £81.0
        // === Declarative result ===
        // Books | B001 | £13.5
        // Electronics | E001 | £45.0
        // Electronics | E002 | £81.0
    }

    private static List<Product> buildCatalogue() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("E001", "Electronics", 50.0, true));
        list.add(new Product("B001", "Books",        15.0, true));
        list.add(new Product("E002", "Electronics",  90.0, true));
        list.add(new Product("G001", "Gifts",        25.0, false)); // unavailable
        return list;
    }
}