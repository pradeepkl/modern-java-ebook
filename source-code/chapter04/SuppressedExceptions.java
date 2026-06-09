// Java 7+
/**
 * Listing 4.17 — SuppressedExceptions.java
 * Demonstrates: Suppressed exceptions when close() fails during try-with-resources
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 7+
 */
package chapter04;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SuppressedExceptions {

    private static final Logger logger =
            Logger.getLogger(SuppressedExceptions.class.getName());

    /**
     * Resource that throws on close — simulates cleanup failure
     * while the try block already failed.
     */
    static class FailingResource implements AutoCloseable {
        @Override
        public void close() throws IOException {
            throw new IOException("Failed to close resource");
        }
    }

    public static void main(String[] args) {
        try (var resource = new FailingResource()) {
            throw new IOException("Primary failure in try block");
        } catch (IOException e) {
            logger.log(Level.WARNING, "Primary failure", e);
        }
        // Output:
        // WARNING: Primary failure
        //   Primary failure in try block
        //   Suppressed: Failed to close resource
    }
}
