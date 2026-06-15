// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 4.21 — LogAndRethrow.java
 * Demonstrates: Smell 4 — logging and rethrowing without adding value
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogAndRethrow {

    private static final Logger logger =
            Logger.getLogger(LogAndRethrow.class.getName());

    // Custom checked exception representing a missing user
    static class UserNotFoundException extends Exception {
        private final String userId;

        UserNotFoundException(String userId) {
            super("User not found: " + userId);
            this.userId = userId;
        }

        String getUserId() {
            return userId;
        }
    }

    // Simulates a user lookup that fails for unknown IDs
    static void loadUser(String userId) throws UserNotFoundException {
        if (userId == null || userId.isBlank() || userId.equals("unknown")) {
            throw new UserNotFoundException(userId);
        }
        logger.info("User loaded: " + userId);
    }

    // Demonstrates the smell: log then rethrow adds no new value
    static void handleUser(String userId) throws UserNotFoundException {
        try {
            loadUser(userId);
        } catch (UserNotFoundException e) {
            // Smell: logging here and rethrowing duplicates the signal
            logger.log(
                    Level.SEVERE,
                    "User not found",
                    e);
            throw e; // caller will likely log again — double logging
        }
    }

    void main() {
        String userId = "unknown";
        try {
            handleUser(userId);
        } catch (UserNotFoundException e) {
            // Caller catches the rethrown exception — already logged above
            logger.log(Level.WARNING,
                    "Caught rethrown exception for userId: " + e.getUserId(), e);
        }
        // Output:
        // SEVERE: User not found (with stack trace)
        // WARNING: Caught rethrown exception for userId: unknown (with stack trace)
    }
}