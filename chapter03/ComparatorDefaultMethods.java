// Java 25+
// Feature shown: Comparator default methods (thenComparing), final in Java 8+

package chapter03;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Listing 3.6 — ComparatorDefaultMethods.java
 * Demonstrates: Comparator default methods (thenComparing) added in Java 8
 * as a real-world example of interface evolution via default methods.
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
public class ComparatorDefaultMethods {

    private static final Logger LOG =
            Logger.getLogger(ComparatorDefaultMethods.class.getName());

    void main() {
        List<String> names = Arrays.asList("John", "Alice", "Bob", "Alice");

        // thenComparing is a default method on Comparator (Java 8+)
        // Sorts by name length first, then alphabetically for equal lengths
        names.sort(
            Comparator.comparingInt(String::length)
                      .thenComparing(Comparator.naturalOrder())
        );

        // Expected: [Bob, John, Alice, Alice]
        LOG.info("Sorted list: " + names);

        // Stream.forEach is a default method on Iterable (Java 8+)
        // Filter names longer than 3 characters and log each
        StringBuilder filtered = new StringBuilder("Names longer than 3 chars: ");
        names.stream()
             .filter(n -> n.length() > 3)   // keeps John, Alice, Alice
             .forEach(n -> filtered.append(n).append(" "));

        LOG.info(filtered.toString().trim());

        // Output:
        // Sorted list: [Bob, John, Alice, Alice]
        // Names longer than 3 chars: John Alice Alice
    }
}