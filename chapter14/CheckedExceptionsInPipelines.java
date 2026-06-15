// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 14.22 — CheckedExceptionsInPipelines.java
 * Demonstrates: Handling checked exceptions inside stream pipelines
 * Chapter 14: Streams: Building Blocks of Declarative Pipelines
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter14;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

public class CheckedExceptionsInPipelines {

    private static final Logger LOG =
            Logger.getLogger(CheckedExceptionsInPipelines.class.getName());

    // Named helper: wraps checked IOException in unchecked UncheckedIOException
    static String readSafely(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    void main() throws IOException {
        // Create two temporary files with known content
        Path p1 = Files.createTempFile("order1", ".txt");
        Path p2 = Files.createTempFile("order2", ".txt");
        Files.writeString(p1, "order-alpha");
        Files.writeString(p2, "order-beta");

        List<Path> paths = List.of(p1, p2);

        // Approach 1: Inline try-catch wraps IOException at the boundary
        List<String> lines2 = paths.stream()
                .map(path -> {
                    try {
                        return Files.readString(path); // checked exception handled here
                    } catch (IOException e) {
                        throw new UncheckedIOException(e); // rethrow as unchecked
                    }
                })
                .toList();

        LOG.info("Inline approach: " + lines2);

        // Approach 2: Named helper method keeps the pipeline clean and readable
        List<String> lines3 = paths.stream()
                .map(CheckedExceptionsInPipelines::readSafely) // clean method reference
                .toList();

        LOG.info("Named helper approach: " + lines3);

        // Cleanup temp files
        Files.deleteIfExists(p1);
        Files.deleteIfExists(p2);

        // Output:
        // INFO: Inline approach: [order-alpha, order-beta]
        // INFO: Named helper approach: [order-alpha, order-beta]
    }
}