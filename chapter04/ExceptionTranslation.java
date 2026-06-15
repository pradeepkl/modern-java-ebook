// Java 25+
// Feature shown: exception translation across layers, final in Java 25+

/**
 * Listing 4.5 — ExceptionTranslation.java
 * Demonstrates: translating low-level exceptions into meaningful domain exceptions
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter04;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionTranslation {

    private static final Logger logger =
            Logger.getLogger(ExceptionTranslation.class.getName());

    // Custom domain exception that wraps lower-level causes
    static class DataAccessException extends RuntimeException {
        DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // Simple User record to represent a domain object
    record User(Long id, String name) {}

    // Simulated repository that throws a checked SQLException
    static class UserRepository {
        User findById(Long userId) throws SQLException {
            if (userId == null || userId <= 0) {
                throw new SQLException("Invalid user ID: " + userId);
            }
            return new User(userId, "Alice");
        }
    }

    private final UserRepository userRepository = new UserRepository();

    // Translates SQLException from the data layer into a domain exception
    public User findUser(Long userId) {
        try {
            return userRepository.findById(userId); // data-layer call
        } catch (SQLException e) {
            // Wrap the low-level exception; preserve the original cause
            throw new DataAccessException(
                    "Failed to load user", e);
        }
    }

    void main() {
        // Successful lookup
        User user = findUser(1L);
        logger.info("Found user: " + user.name()); // Found user: Alice

        // Trigger exception translation
        try {
            findUser(-1L); // causes SQLException in the repository
        } catch (DataAccessException e) {
            // Domain exception surfaced; original cause is preserved
            logger.log(Level.SEVERE,
                    "DataAccessException caught: " + e.getMessage()
                    + " | cause: " + e.getCause().getMessage());
        }

        // Output:
        // INFO: Found user: Alice
        // SEVERE: DataAccessException caught: Failed to load user | cause: Invalid user ID: -1
    }
}