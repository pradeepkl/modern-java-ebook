// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 4.22 — LosingOriginalCause.java
 * Demonstrates: the smell of discarding the original exception cause when wrapping
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter04;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LosingOriginalCause {

    private static final Logger logger = Logger.getLogger(LosingOriginalCause.class.getName());

    // Custom wrapper exception for data access failures
    static class DataAccessException extends RuntimeException {
        DataAccessException(String message) {
            super(message); // original cause is NOT passed — this is the smell
        }

        DataAccessException(String message, Throwable cause) {
            super(message, cause); // correct: cause is preserved
        }
    }

    // Simulates a repository save that throws a checked SQLException
    void save(String entity) throws SQLException {
        throw new SQLException("Constraint violation on table ORDERS");
    }

    // BAD: original SQLException is discarded — cause chain is broken
    void saveWithoutCause(String entity) {
        try {
            save(entity);
        } catch (SQLException e) {
            // Original SQLException is discarded — avoid this
            throw new DataAccessException("Save failed");
        }
    }

    // GOOD: original SQLException is preserved as the cause
    void saveWithCause(String entity) {
        try {
            save(entity);
        } catch (SQLException e) {
            // Cause is passed — full diagnostic chain is preserved
            throw new DataAccessException("Save failed", e);
        }
    }

    void main() {
        LosingOriginalCause demo = new LosingOriginalCause();

        // Demonstrate the bad pattern: cause is lost
        try {
            demo.saveWithoutCause("ORDER-99");
        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "Bad pattern — cause is null: {0}",
                    String.valueOf(e.getCause()));
        }

        // Demonstrate the good pattern: cause is preserved
        try {
            demo.saveWithCause("ORDER-99");
        } catch (DataAccessException e) {
            logger.log(Level.SEVERE, "Good pattern — cause preserved", e);
        }

        // Output:
        // WARNING: Bad pattern — cause is null: null
        // SEVERE: Good pattern — cause preserved
        //   Caused by: java.sql.SQLException: Constraint violation on table ORDERS
    }
}