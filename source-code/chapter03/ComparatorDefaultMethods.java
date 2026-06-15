// Java 25+
// Feature shown: Comparator default methods (thenComparing), final in Java 8+

/**
 * Listing 3.6 — ComparatorDefaultMethods.java
 * Demonstrates: Comparator.thenComparing as a default method enabling chained
 * sort criteria without breaking existing Comparator implementations.
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter03;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

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

        // Expected: [Bob, John, Alice, Alice] — length then alpha
        LOG.info("Sorted list: " + names);

        // forEach on Iterable is also a default method (Java 8+)
        // Filters names longer than 3 characters and logs each
        names.stream()
             .filter(n -> n.length() > 3)
             .forEach(name -> LOG.info("Name longer than 3 chars: " + name));

        // Output:
        // Sorted list: [Bob, John, Alice, Alice]
        // Name longer than 3 chars: John
        // Name longer than 3 chars: Alice
        // Name longer than 3 chars: Alice
    }
}