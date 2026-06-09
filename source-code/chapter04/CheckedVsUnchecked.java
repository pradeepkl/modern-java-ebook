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
import java.nio.file.Files;
import java.nio.file.Path;

public class CheckedVsUnchecked {

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
            System.out.println("File content: " + content);
        } catch (IOException e) {
            // Meaningful recovery: log and use a fallback value
            System.out.println("File unavailable, using fallback: " + e.getMessage());
        }

        // Demonstrate unchecked exception — valid input succeeds
        String user = demo.fetchUser(42L);
        System.out.println("Fetched: " + user); // Fetched: User-42

        // Demonstrate unchecked exception — null input reveals a programming error
        try {
            demo.fetchUser(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Programming error caught: " + e.getMessage());
        }

        // Output:
        // File unavailable, using fallback: nonexistent.txt (No such file or directory)
        // Fetched: User-42
        // Programming error caught: User ID cannot be null
    }
}