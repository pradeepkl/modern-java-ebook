// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+
package chapter01;

import java.util.logging.Logger;

/**
 * Listing 1.3 — ProductTraditional.java
 * Demonstrates: traditional verbose POJO with explicit constructor, getters,
 * equals, hashCode, and toString (pre-records style, for contrast)
 * Chapter 1: Modern Java: A Shift in Mindset
 * Requires: Java 25+ (instance main method via JEP 512)
 */
public class ProductTraditional {

    private static final Logger log =
            Logger.getLogger(ProductTraditional.class.getName());

    // Traditional class requires explicit fields, constructor, getters, and utility methods
    static class Product {
        private final String name;
        private final double price;
        private final int stock;

        public Product(String name, double price, int stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        public String getName()  { return name; }
        public double getPrice() { return price; }
        public int getStock()    { return stock; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Product p)) return false;
            return Double.compare(p.price, price) == 0
                && stock == p.stock
                && name.equals(p.name);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + Double.hashCode(price);
            result = 31 * result + stock;
            return result;
        }

        // toString must be written and maintained manually — pure boilerplate
        @Override
        public String toString() {
            return "Product[name=" + name
                + ", price=" + price
                + ", stock=" + stock + "]";
        }
    }

    void main() {
        Product p = new Product("Widget", 9.99, 100);
        log.info(String.valueOf(p)); // logs the manually written toString result
        // Output: Product[name=Widget, price=9.99, stock=100]
    }
}