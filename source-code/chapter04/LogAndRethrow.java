// Java 8+
/**
 * Listing 4.20 — LogAndRethrow.java
 * Demonstrates: Logging and rethrowing an exception without adding value (smell)
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 8+
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogAndRethrow {

    // Simulated logger using java.util.logging
    private static final Logger logger = Logger.getLogger(LogAndRethrow.class.getName());

    // Custom checked exception representing a missing user
    static class UserNotFoundException extends Exception {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    // Simulates loading a user by ID — throws if not found
    static void loadUser(int userId) throws UserNotFoundException {
        if (userId <= 0) {
            throw new UserNotFoundException("No user found with ID: " + userId);
        }
        System.out.println("User " + userId + " loaded successfully.");
    }

    // Smell: logs the exception AND rethrows it — caller will log it again
    static void fetchUser(int userId) throws UserNotFoundException {
        try {
            loadUser(userId); // attempt to load user
        } catch (UserNotFoundException e) {
            // Logs here — but then rethrows, risking duplicate log entries
            logger.log(Level.SEVERE, "User not found", e);
            throw e; // rethrowing without adding context or wrapping
        }
    }

    public static void main(String[] args) {
        // Demonstrate successful load
        try {
            fetchUser(42);
        } catch (UserNotFoundException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // Demonstrate log-and-rethrow smell with invalid ID
        try {
            fetchUser(-1); // triggers UserNotFoundException
        } catch (UserNotFoundException e) {
            // Caller also handles — exception was already logged inside fetchUser
            System.out.println("Caught after rethrow: " + e.getMessage());
        }

        // Output:
        // User 42 loaded successfully.
        // SEVERE: User not found (logged inside fetchUser)
        // Caught after rethrow: No user found with ID: -1
    }
}