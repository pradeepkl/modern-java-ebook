// Java 8+
/**
 * Listing 4.5 — ExceptionTranslation.java
 * Demonstrates: Translating low-level exceptions across layer boundaries
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 8+
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
        public DataAccessException(String message, Throwable cause) {
            super(message, cause); // Always preserve the original cause
        }
    }

    // Simulated domain object
    static class User {
        private final long id;
        User(long id) { this.id = id; }
        @Override public String toString() { return "User{id=" + id + "}"; }
    }

    // Simulated repository that throws a checked SQLException
    static class UserRepository {
        User findById(Long userId) throws SQLException {
            if (userId == null || userId <= 0) {
                // Simulate a database-level failure
                throw new SQLException("No record found for id: " + userId);
            }
            return new User(userId); // Simulate a successful lookup
        }
    }

    private final UserRepository userRepository = new UserRepository();

    // Translates SQLException into a meaningful domain exception
    public User findUser(Long userId) {
        try {
            return userRepository.findById(userId); // May throw SQLException
        } catch (SQLException e) {
            // Wrap low-level exception; preserve cause for debugging
            throw new DataAccessException("Failed to load user", e);
        }
    }

    public static void main(String[] args) {
        ExceptionTranslation service = new ExceptionTranslation();

        // Successful lookup — no exception expected
        User user = service.findUser(42L);
        logger.log(Level.INFO, "Found: {0}", user);

        // Failed lookup — SQLException translated to DataAccessException
        try {
            service.findUser(-1L);
        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "Caught domain exception: {0}", e.getMessage());
            logger.log(Level.WARNING, "Root cause: {0}", e.getCause().getMessage());
        }

        // Output:
        // INFO: Found: User{id=42}
        // WARNING: Caught domain exception: Failed to load user
        // WARNING: Root cause: No record found for id: -1
    }
}
