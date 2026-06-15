// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

package chapter14;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Listing 14.21 — InfiniteStreams.java
 * Demonstrates: Infinite streams with Stream.generate() and Stream.iterate(),
 * and the necessity of limiting operations such as limit() to prevent
 * unbounded execution.
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
public class InfiniteStreams {

    private static final Logger LOG = Logger.getLogger(InfiniteStreams.class.getName());

    void main() {

        // Bounded: generate 5 random doubles and collect to a list
        List<Double> randomValues = Stream.generate(Math::random)
                .limit(5)                          // without limit() this runs forever
                .toList();

        LOG.info("Five random values from Stream.generate:");
        randomValues.forEach(v -> LOG.info(String.format("  %.6f", v)));

        // Stream.iterate: produce the first 6 powers of 2
        List<Long> powersOfTwo = Stream.iterate(1L, n -> n * 2)
                .limit(6)                          // limit() makes the infinite stream finite
                .toList();

        LOG.info("First six powers of two from Stream.iterate:");
        powersOfTwo.forEach(v -> LOG.info("  " + v));

        // Stream.iterate with a predicate (Java 9+): stops when value exceeds 100
        List<Long> bounded = Stream.iterate(1L, n -> n <= 100, n -> n * 2)
                .toList();                         // predicate acts as a built-in limit

        LOG.info("Powers of two up to 100 (iterate with predicate):");
        bounded.forEach(v -> LOG.info("  " + v));

        // Output:
        // Five random values from Stream.generate:
        //   0.731245
        //   0.198034
        //   0.562901
        //   0.047812
        //   0.883456
        // First six powers of two from Stream.iterate:
        //   1
        //   2
        //   4
        //   8
        //   16
        //   32
        // Powers of two up to 100 (iterate with predicate):
        //   1
        //   2
        //   4
        //   8
        //   16
        //   32
        //   64
    }
}