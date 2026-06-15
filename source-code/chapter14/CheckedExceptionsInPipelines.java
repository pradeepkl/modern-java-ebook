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
            throw new UncheckedIOException(e); // rethrow as unchecked
        }
    }

    void main() throws IOException {
        // Create two temporary files with known content
        Path p1 = Files.createTempFile("order1", ".txt");
        Path p2 = Files.createTempFile("order2", ".txt");
        Files.writeString(p1, "ORDER-001");
        Files.writeString(p2, "ORDER-002");

        List<Path> paths = List.of(p1, p2);

        // Approach 1: inline try-catch wraps IOException at the boundary
        List<String> lines2 = paths.stream()
                .map(path -> {
                    try {
                        return Files.readString(path); // checked exception handled
                    } catch (IOException e) {
                        throw new UncheckedIOException(e); // promote to unchecked
                    }
                })
                .toList();

        LOG.info("Approach 1 (inline): " + lines2);

        // Approach 2: delegate to a named helper method for a cleaner pipeline
        List<String> lines3 = paths.stream()
                .map(CheckedExceptionsInPipelines::readSafely) // clean reference
                .toList();

        LOG.info("Approach 2 (helper): " + lines3);

        // Cleanup temp files
        Files.deleteIfExists(p1);
        Files.deleteIfExists(p2);

        // Output:
        // INFO: Approach 1 (inline): [ORDER-001, ORDER-002]
        // INFO: Approach 2 (helper): [ORDER-001, ORDER-002]
    }
}