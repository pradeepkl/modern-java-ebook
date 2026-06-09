// Java 8+
/**
 * Listing 4.7 — CheckedVsUnchecked.java
 * Demonstrates: Checked vs unchecked exceptions and when to use each
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 8+
 */
package chapter04;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckedVsUnchecked {

    private static final Logger logger =
            Logger.getLogger(CheckedVsUnchecked.class.getName());

    // Checked — recoverable external condition
    // Caller can retry, use a fallback, or report the failure
    public String readFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.readLine(); // May fail due to missing file, permissions, etc.
        }
    }

    // Unchecked — programming error or broken assumption
    // Recovery is not meaningful — fix the code instead
    public String fetchUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID cannot be null"); // Caller passed bad data — a code defect
        }
        return "User-" + userId; // Simulated fetch logic
    }

    public static void main(String[] args) {
        CheckedVsUnchecked demo = new CheckedVsUnchecked();

        // Demonstrate checked exception — caller must handle or propagate
        try {
            String content = demo.readFile("nonexistent.txt");
            logger.log(Level.INFO, "File content: {0}", content);
        } catch (IOException e) {
            // Meaningful recovery: log and use a fallback value
            logger.log(Level.WARNING, "File unavailable, using fallback: {0}", e.getMessage());
        }

        // Demonstrate unchecked exception — valid input succeeds
        String user = demo.fetchUser(42L);
        logger.log(Level.INFO, "Fetched: {0}", user); // Fetched: User-42

        // Demonstrate unchecked exception — null input reveals a programming error
        try {
            demo.fetchUser(null);
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Programming error caught: {0}", e.getMessage());
        }

        // Output:
        // WARNING: File unavailable, using fallback: nonexistent.txt (No such file or directory)
        // INFO: Fetched: User-42
        // WARNING: Programming error caught: User ID cannot be null
    }
}
