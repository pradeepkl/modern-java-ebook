// Java 25+
// Feature shown: checked vs unchecked exceptions, final in Java 8+

/**
 * Listing 4.7 — CheckedVsUnchecked.java
 * Demonstrates: Checked exceptions for recoverable external conditions
 *               and unchecked exceptions for programming errors.
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckedVsUnchecked {

    private static final Logger logger =
            Logger.getLogger(CheckedVsUnchecked.class.getName());

    // Checked — recoverable external condition
    // Caller can retry, use a fallback, or report the failure
    public void readFile(String filePath) throws IOException {
        // IOException signals an external failure the caller can handle
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        logger.info("Read " + bytes.length + " bytes from " + filePath);
    }

    // Unchecked — programming error or broken assumption
    // Recovery is not meaningful — fix the code instead
    public void fetchUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID cannot be null"); // fail fast on bad input
        }
        // Fetch logic would follow here
        logger.info("Fetching user with ID: " + userId);
    }

    void main() {
        CheckedVsUnchecked demo = new CheckedVsUnchecked();

        // Demonstrate checked exception handling — caller decides recovery
        try {
            demo.readFile("nonexistent-file.txt");
        } catch (IOException e) {
            // Caller recovers by logging and continuing
            logger.log(Level.WARNING,
                    "File not found, using fallback: " + e.getMessage());
        }

        // Demonstrate unchecked exception — signals a programming error
        try {
            demo.fetchUser(null); // passing null violates the contract
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE,
                    "Invalid argument detected: " + e.getMessage());
        }

        // Valid call — no exception expected
        demo.fetchUser(42L);

        // Output:
        // WARNING: File not found, using fallback: nonexistent-file.txt ...
        // SEVERE:  Invalid argument detected: User ID cannot be null
        // INFO:    Fetching user with ID: 42
    }
}