// Java 25+
// Feature shown: var in try-with-resources with multiple resources, final in Java 10+

/**
 * Listing 4.16 — MultipleResources.java
 * Demonstrates: Multiple AutoCloseable resources in a single try-with-resources
 *               statement using var, closed in reverse order of declaration.
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter04;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class MultipleResources {

    private static final Logger logger =
            Logger.getLogger(MultipleResources.class.getName());

    // Minimal in-memory DataSource stub for demonstration purposes
    static class StubDataSource implements DataSource {
        public Connection getConnection() throws SQLException {
            throw new SQLException("No real database available — demo only");
        }
        public Connection getConnection(String u, String p) throws SQLException { return getConnection(); }
        public java.io.PrintWriter getLogWriter() { return null; }
        public void setLogWriter(java.io.PrintWriter pw) {}
        public void setLoginTimeout(int s) {}
        public int getLoginTimeout() { return 0; }
        public java.util.logging.Logger getParentLogger() { return logger; }
        public <T> T unwrap(Class<T> i) { return null; }
        public boolean isWrapperFor(Class<?> i) { return false; }
    }

    // Custom checked exception wrapping a data-access failure
    static class DataAccessException extends Exception {
        DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    void queryUsers(DataSource dataSource) throws DataAccessException {
        var sql = "SELECT id, name FROM users";

        // All three resources are closed automatically in reverse order:
        // resultSet -> statement -> connection
        try (var connection = dataSource.getConnection();
             var statement  = connection.prepareStatement(sql);
             var resultSet  = statement.executeQuery()) {

            while (resultSet.next()) {
                logger.log(Level.INFO, () -> "Row: " + resultSet);
            }

        } catch (SQLException e) {
            // Wrap low-level SQL failure in a domain exception
            throw new DataAccessException("Query failed", e);
        }
    }

    void main() {
        var dataSource = new StubDataSource();
        try {
            queryUsers(dataSource);
        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "Caught expected demo exception: " + e.getMessage());
        }
        // Output:
        // WARNING: Caught expected demo exception: Query failed
    }
}