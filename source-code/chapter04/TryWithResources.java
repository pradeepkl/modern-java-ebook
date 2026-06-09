// Java 9+
package chapter04;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listing 4.15 — TryWithResources.java
 * Demonstrates: Modern try-with-resources using var (Java 9+)
 * and lambda-based lazy logging with java.util.logging
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 9+
 */
public class TryWithResources {

    private static final Logger logger =
            Logger.getLogger(TryWithResources.class.getName());

    public static void main(String[] args) {
        String filename = "data.txt";

        // Create a temporary file to read from
        try (var writer = new FileWriter(filename)) {
            writer.write("Hello, Modern Java!\n");
            writer.write("Try-with-resources is clean.\n");
            writer.write("Resources close automatically.\n");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not create file", e);
            return;
        }

        // Java 9+: var infers BufferedReader type in try-with-resources
        try (var reader = new BufferedReader(
                new FileReader(filename))) {
            String line;
            // Read each line until end of file
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Print each line
            }
        } catch (IOException e) {
            // Lambda supplier defers string construction — only evaluated if logged
            logger.log(Level.WARNING,
                    () -> "Failed to read file: " + filename);
        }

        // Clean up the temp file
        new java.io.File(filename).delete();

        // Output:
        // Hello, Modern Java!
        // Try-with-resources is clean.
        // Resources close automatically.
    }
}