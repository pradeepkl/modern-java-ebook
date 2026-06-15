// Java 25+
// Feature shown: var in try-with-resources, final in Java 10+
/**
 * Listing 4.15 — TryWithResources.java
 * Demonstrates: try-with-resources with var for automatic resource management
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter04;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TryWithResources {

    private static final Logger logger =
            Logger.getLogger(TryWithResources.class.getName());

    void main() {
        // Create a temporary file to read from
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("data", ".txt");
            try (var writer = new PrintWriter(tempFile.toFile())) {
                writer.println("Hello from line one");
                writer.println("Hello from line two");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create temp file", e);
            return;
        }

        String filePath = tempFile.toString();

        // var infers BufferedReader; resource is closed automatically
        try (var reader = new BufferedReader(
                new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.log(Level.INFO, line); // log each line read
            }
        } catch (IOException e) {
            logger.log(
                    Level.WARNING,
                    () -> "Failed to read file: " + filePath);
        }

        // Clean up temp file
        try {
            Files.deleteIfExists(tempFile);
        } catch (IOException ignored) {
            // best-effort cleanup
        }

        // Output:
        // INFO: Hello from line one
        // INFO: Hello from line two
    }
}