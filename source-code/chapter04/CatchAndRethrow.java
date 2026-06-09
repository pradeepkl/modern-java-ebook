// Java 8+
/**
 * Listing 4.4 — CatchAndRethrow.java
 * Demonstrates: Catching specific exceptions and rethrowing as meaningful domain exceptions
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 8+
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CatchAndRethrow {

    // Standard Java logger — no external dependencies
    private static final Logger logger = Logger.getLogger(CatchAndRethrow.class.getName());

    // Custom domain exception wrapping root cause with context
    static class DomainException extends RuntimeException {
        public DomainException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // Simulated checked exceptions representing integration-layer failures
    static class SpecificException extends Exception {
        public SpecificException(String msg) { super(msg); }
    }

    static class AnotherException extends Exception {
        public AnotherException(String msg) { super(msg); }
    }

    // Simulates a method that may throw low-level exceptions
    static void riskyOperation(int scenario) throws SpecificException, AnotherException {
        if (scenario == 1) throw new SpecificException("DB constraint violated");
        if (scenario == 2) throw new AnotherException("Timeout during processing");
    }

    // Catches low-level exceptions and rethrows as domain exceptions with context
    static void processData(int scenario) {
        try {
            riskyOperation(scenario); // Core logic call
        } catch (SpecificException e) {
            // Rethrow with contextual domain message; original cause preserved
            throw new DomainException("Contextual message: data integrity issue", e);
        } catch (AnotherException e) {
            // Log before rethrowing so the failure is visible in diagnostics
            logger.log(Level.SEVERE, "An error occurred while processing", e);
            throw new DomainException("Processing failed: operation timed out", e);
        }
    }

    public static void main(String[] args) {
        for (int scenario = 1; scenario <= 3; scenario++) {
            try {
                processData(scenario);
                System.out.println("Scenario " + scenario + ": completed successfully");
            } catch (DomainException e) {
                // Caller sees domain-level message; root cause still accessible
                System.out.println("Caught DomainException: " + e.getMessage());
                System.out.println("  Caused by: " + e.getCause().getMessage());
            }
        }
        // Output:
        // Caught DomainException: Contextual message: data integrity issue
        //   Caused by: DB constraint violated
        // SEVERE: An error occurred while processing
        // Caught DomainException: Processing failed: operation timed out
        //   Caused by: Timeout during processing
        // Scenario 3: completed successfully
    }
}