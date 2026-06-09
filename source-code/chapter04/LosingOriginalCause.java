// Java 8+
/**
 * Listing 4.21 — LosingOriginalCause.java
 * Demonstrates: The smell of discarding the original exception cause
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 8+
 */
package chapter04;

import java.sql.SQLException;

public class LosingOriginalCause {

    // Custom exception for data access failures
    static class DataAccessException extends RuntimeException {
        public DataAccessException(String message) {
            super(message); // original cause is NOT preserved
        }

        public DataAccessException(String message, Throwable cause) {
            super(message, cause); // original cause IS preserved
        }
    }

    // Simulates a repository save that throws SQLException
    static void save(String entity) throws SQLException {
        throw new SQLException("Duplicate key violation for: " + entity);
    }

    // BAD: original cause is discarded
    static void saveWithoutCause(String entity) {
        try {
            save(entity);
        } catch (SQLException e) {
            // Original SQLException is discarded — avoid this
            throw new DataAccessException("Save failed");
        }
    }

    // GOOD: original cause is preserved as the chained exception
    static void saveWithCause(String entity) {
        try {
            save(entity);
        } catch (SQLException e) {
            // Wraps with cause — stack trace and root error are retained
            throw new DataAccessException("Save failed", e);
        }
    }

    public static void main(String[] args) {
        // Demonstrate losing the cause
        System.out.println("=== Without cause (BAD) ===");
        try {
            saveWithoutCause("UserEntity");
        } catch (DataAccessException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Cause: " + e.getCause()); // null — root cause lost
        }

        // Demonstrate preserving the cause
        System.out.println("\n=== With cause (GOOD) ===");
        try {
            saveWithCause("UserEntity");
        } catch (DataAccessException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Cause: " + e.getCause()); // original SQLException visible
        }

        // Output:
        // === Without cause (BAD) ===
        // Caught: Save failed
        // Cause: null
        //
        // === With cause (GOOD) ===
        // Caught: Save failed
        // Cause: java.sql.SQLException: Duplicate key violation for: UserEntity
    }
}