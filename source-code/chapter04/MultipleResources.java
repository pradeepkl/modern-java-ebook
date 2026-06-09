// Java 9+
/**
 * Listing 4.16 — MultipleResources.java
 * Demonstrates: Multiple AutoCloseable resources in a single try-with-resources statement
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 9+
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MultipleResources {

    private static final Logger logger =
            Logger.getLogger(MultipleResources.class.getName());

    // Simulated lightweight AutoCloseable resources for demonstration
    static class FakeConnection implements AutoCloseable {
        FakeConnection() { logger.log(Level.FINE, "Connection opened"); }
        FakeStatement prepareStatement(String sql) { return new FakeStatement(sql); }
        @Override public void close() { logger.log(Level.FINE, "Connection closed"); }
    }

    static class FakeStatement implements AutoCloseable {
        private final String sql;
        FakeStatement(String sql) {
            this.sql = sql;
            logger.log(Level.FINE, "Statement prepared: {0}", sql);
        }
        FakeResultSet executeQuery() { return new FakeResultSet(); }
        @Override public void close() { logger.log(Level.FINE, "Statement closed"); }
    }

    static class FakeResultSet implements AutoCloseable {
        private int row = 0;
        FakeResultSet() { logger.log(Level.FINE, "ResultSet opened"); }
        boolean next() { return row++ < 2; } // simulate 2 rows
        String getString(String col) { return "row-" + row + "-" + col; }
        @Override public void close() { logger.log(Level.FINE, "ResultSet closed"); }
    }

    static class DataAccessException extends RuntimeException {
        DataAccessException(String msg, Throwable cause) { super(msg, cause); }
    }

    public static void main(String[] args) {
        var sql = "SELECT id, name FROM users";

        // Multiple resources declared in one try; closed in reverse order
        try (var connection = new FakeConnection();
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Process each row from the result set
                logger.log(Level.INFO, "  Row: {0}", resultSet.getString("name"));
            }
        } catch (Exception e) {
            throw new DataAccessException("Query failed", e);
        }

        logger.log(Level.INFO,
                "All resources closed automatically in reverse order.");
        // Output:
        // FINE: Connection opened
        // FINE: Statement prepared: SELECT id, name FROM users
        // FINE: ResultSet opened
        // INFO:   Row: row-1-name
        // INFO:   Row: row-2-name
        // FINE: ResultSet closed
        // FINE: Statement closed
        // FINE: Connection closed
        // INFO: All resources closed automatically in reverse order.
    }
}
