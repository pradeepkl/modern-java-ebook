// Java 8+
/**
 * Listing 4.5 — ExceptionTranslation.java
 * Demonstrates: Translating low-level exceptions across layer boundaries
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 8+
 */
package chapter04;

import java.sql.SQLException;

public class ExceptionTranslation {

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
        System.out.println("Found: " + user);

        // Failed lookup — SQLException translated to DataAccessException
        try {
            service.findUser(-1L);
        } catch (DataAccessException e) {
            System.out.println("Caught domain exception: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause().getMessage());
        }

        // Output:
        // Found: User{id=42}
        // Caught domain exception: Failed to load user
        // Root cause: No record found for id: -1
    }
}