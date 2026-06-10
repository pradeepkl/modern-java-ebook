// Java 8+
/**
 * Listing 11.7 — FluentTransformations.java
 * Demonstrates: Declarative vs imperative list transformation using removeIf,
 *               replaceAll, sort, and forEach on a mutable catalogue list
 * Chapter 11: Declarative Data Transformations
 * Requires: Java 8+
 */
package chapter11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class FluentTransformations {

    private static final Logger log = Logger.getLogger(FluentTransformations.class.getName());

    // Simple Product record to represent catalogue items
    record Product(String productId, String category, double price, boolean available) {}

    public static void main(String[] args) {

        // --- Imperative approach ---
        List<Product> catalogue = buildCatalogue();

        // ❌ Imperative — mechanics dominate: loop, condition, construction, sorting all mixed
        List<Product> result = new ArrayList<>();
        for (Product p : catalogue) {
            if (p.available()) {
                result.add(new Product(p.productId(), p.category(), p.price() * 0.9, p.available()));
            }
        }
        result.sort((a, b) -> Double.compare(a.price(), b.price()));
        log.info("=== Imperative result ===");
        result.forEach(p -> log.info(p.category() + " | " + p.productId() + " | £" + p.price()));

        // --- Declarative approach ---
        List<Product> declarativeCatalogue = new ArrayList<>(buildCatalogue());

        // ✅ Each line is one sentence — intent is immediately visible

        // Remove unavailable products
        declarativeCatalogue.removeIf(p -> !p.available());

        // Apply 10% discount to every remaining product
        declarativeCatalogue.replaceAll(p ->
                new Product(p.productId(), p.category(), p.price() * 0.9, p.available()));

        // Sort by category, then by price within each category
        declarativeCatalogue.sort(
                Comparator.comparing(Product::category)
                        .thenComparingDouble(Product::price));

        log.info("=== Declarative result ===");
        // Print each product in a readable format
        declarativeCatalogue.forEach(p ->
                log.info(p.category() + " | " + p.productId() + " | £" + p.price()));

        // Output:
        // === Imperative result ===
        // Books | B01 | £8.99
        // Electronics | E02 | £89.99
        // Electronics | E01 | £179.99
        // === Declarative result ===
        // Books | B01 | £8.99
        // Electronics | E02 | £89.99
        // Electronics | E01 | £179.99
    }

    private static List<Product> buildCatalogue() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("E01", "Electronics", 199.99, true));
        list.add(new Product("B01", "Books",         9.99, true));
        list.add(new Product("E02", "Electronics",  99.99, true));
        list.add(new Product("C01", "Clothing",     49.99, false)); // unavailable
        return list;
    }
}