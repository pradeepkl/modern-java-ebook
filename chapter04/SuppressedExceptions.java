// Java 25+
// Feature shown: suppressed exceptions with try-with-resources, final in Java 25+

/**
 * Listing 4.17 — SuppressedExceptions.java
 * Demonstrates: suppressed exceptions attached via try-with-resources when
 * both the try block and close() throw; retrieved via getSuppressed()
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter04;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SuppressedExceptions {

    private static final Logger logger =
            Logger.getLogger(SuppressedExceptions.class.getName());

    /** A resource whose close() always throws to simulate cleanup failure. */
    static class FailingResource implements AutoCloseable {
        @Override
        public void close() throws IOException {
            throw new IOException("Failure during resource close()");
        }
    }

    void main() {
        try (var resource = new FailingResource()) {
            // Primary exception thrown inside the try block
            throw new IOException("Primary failure in try block");
        } catch (IOException e) {
            // e is the primary exception; close() exception is suppressed
            logger.log(Level.WARNING, "Primary failure caught: " + e.getMessage());

            Throwable[] suppressed = e.getSuppressed(); // retrieve suppressed list
            if (suppressed.length > 0) {
                for (Throwable s : suppressed) {
                    // Each suppressed exception is logged separately
                    logger.log(Level.WARNING,
                            "Suppressed exception: " + s.getMessage());
                }
            } else {
                logger.log(Level.INFO, "No suppressed exceptions found.");
            }
        }

        // Output:
        // WARNING: Primary failure caught: Primary failure in try block
        // WARNING: Suppressed exception: Failure during resource close()
    }
}