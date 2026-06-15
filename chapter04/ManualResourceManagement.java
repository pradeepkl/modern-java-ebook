// Java 25+
// Feature shown: manual resource management (pre-Java 7 pattern), final in Java 25+

/**
 * Listing 4.14 — ManualResourceManagement.java
 * Demonstrates: Manual resource management using null checks and finally blocks,
 * the verbose Java 6 and earlier pattern replaced by try-with-resources in Java 7.
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter04;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManualResourceManagement {

    private static final Logger logger =
            Logger.getLogger(ManualResourceManagement.class.getName());

    void main() {
        // Create a temporary file so the demo has something to read
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("data", ".txt");
            Files.writeString(tempFile, "line one\nline two\nline three\n");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not create temp file", e);
            return;
        }

        // Java 6 and earlier — manual resource management
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new FileReader(tempFile.toFile())); // open the resource manually
            String line;
            while ((line = reader.readLine()) != null) {
                logger.log(Level.INFO, line); // process each line
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to read file", e);
        } finally {
            // Must close manually; null check required to avoid NullPointerException
            if (reader != null) {
                try {
                    reader.close(); // close in finally to guarantee execution
                } catch (IOException e) {
                    // close failure silently ignored — common pre-Java 7 pattern
                }
            }
        }

        // Clean up the temporary file
        try {
            Files.deleteIfExists(tempFile);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not delete temp file", e);
        }

        // Output:
        // INFO: line one
        // INFO: line two
        // INFO: line three
    }
}