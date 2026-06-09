// Java 6+ (demonstrating pre-Java-7 manual resource management pattern)
/**
 * Listing 4.14 — ManualResourceManagement.java
 * Demonstrates: Java 6 and earlier manual resource management with explicit
 *               null checks and nested try-catch in finally blocks
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 6+
 */
package chapter04;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManualResourceManagement {

    private static final Logger logger =
            Logger.getLogger(ManualResourceManagement.class.getName());

    // Creates a temporary file with sample content for demonstration
    private static String createSampleFile() throws IOException {
        String path = System.getProperty("java.io.tmpdir") + "/data.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
            writer.println("Hello, World!");
            writer.println("Manual resource management");
            writer.println("Java 6 style");
        }
        return path;
    }

    public static void main(String[] args) {
        String filePath;
        try {
            filePath = createSampleFile(); // prepare sample file
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not create sample file: {0}", e.getMessage());
            return;
        }

        // Java 6 and earlier — manual resource management
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new FileReader(filePath));   // open resource manually
            String line;
            while ((line = reader.readLine()) != null) {
                logger.log(Level.INFO, line);        // process each line
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to read file", e); // handle read errors
        } finally {
            if (reader != null) {                // null check required
                try {
                    reader.close();              // explicit close call
                } catch (IOException e) {
                    // ignored — close failure swallowed silently
                }
            }
        }

        logger.log(Level.INFO, "Resource closed manually in finally block.");
        // Output:
        // INFO: Hello, World!
        // INFO: Manual resource management
        // INFO: Java 6 style
        // INFO: Resource closed manually in finally block.
    }
}
