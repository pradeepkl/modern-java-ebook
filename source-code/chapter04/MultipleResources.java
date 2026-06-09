// Java 9+
/**
 * Listing 4.16 — MultipleResources.java
 * Demonstrates: Multiple AutoCloseable resources in a single try-with-resources statement
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 9+
 */
package chapter04;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.IOException;

public class MultipleResources {

    // Simulated lightweight AutoCloseable resources for demonstration
    static class FakeConnection implements AutoCloseable {
        FakeConnection() { System.out.println("Connection opened"); }
        FakeStatement prepareStatement(String sql) { return new FakeStatement(sql); }
        @Override public void close() { System.out.println("Connection closed"); }
    }

    static class FakeStatement implements AutoCloseable {
        private final String sql;
        FakeStatement(String sql) {
            this.sql = sql;
            System.out.println("Statement prepared: " + sql);
        }
        FakeResultSet executeQuery() { return new FakeResultSet(); }
        @Override public void close() { System.out.println("Statement closed"); }
    }

    static class FakeResultSet implements AutoCloseable {
        private int row = 0;
        FakeResultSet() { System.out.println("ResultSet opened"); }
        boolean next() { return row++ < 2; } // simulate 2 rows
        String getString(String col) { return "row-" + row + "-" + col; }
        @Override public void close() { System.out.println("ResultSet closed"); }
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
                System.out.println("  Row: " + resultSet.getString("name"));
            }
        } catch (Exception e) {
            throw new DataAccessException("Query failed", e);
        }

        System.out.println("All resources closed automatically in reverse order.");
        // Output:
        // Connection opened
        // Statement prepared: SELECT id, name FROM users
        // ResultSet opened
        //   Row: row-1-name
        //   Row: row-2-name
        // ResultSet closed
        // Statement closed
        // Connection closed
        // All resources closed automatically in reverse order.
    }
}