// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 14.21 — InfiniteStreams.java
 * Demonstrates: Infinite streams with Stream.generate() and the need for limit()
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter14;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class InfiniteStreams {

    private static final Logger LOG = Logger.getLogger(InfiniteStreams.class.getName());

    void main() {

        // Stream.generate() without limit() runs forever — do NOT call this:
        // Stream.generate(Math::random).forEach(System.out::println);

        // Bounded: limit(10) short-circuits the infinite source
        List<Double> tenRandomValues = Stream.generate(Math::random)
                .limit(10)                      // stops after 10 elements
                .toList();                      // collect to list, not forEach

        LOG.info("Ten random values collected:");
        for (int i = 0; i < tenRandomValues.size(); i++) {
            LOG.info(String.format("  [%2d] %.6f", i + 1, tenRandomValues.get(i)));
        }

        // Stream.iterate() also produces an infinite stream
        // Use limit() or a predicate overload to bound it
        List<Integer> powersOfTwo = Stream.iterate(1, n -> n * 2)
                .limit(8)                       // first 8 powers of two
                .toList();

        LOG.info("First 8 powers of two: " + powersOfTwo);

        // Stream.iterate with a predicate (Java 9+) — naturally bounded
        List<Integer> underThousand = Stream.iterate(1, n -> n < 1000, n -> n * 2)
                .toList();

        LOG.info("Powers of two under 1000: " + underThousand);

        // Output:
        // Ten random values collected:
        //   [ 1] 0.713452
        //   [ 2] 0.234891
        //   ...  (10 random doubles)
        // First 8 powers of two: [1, 2, 4, 8, 16, 32, 64, 128]
        // Powers of two under 1000: [1, 2, 4, 8, 16, 32, 64, 128, 256, 512]
    }
}