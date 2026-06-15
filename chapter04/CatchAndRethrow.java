// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listing 4.4 — CatchAndRethrow.java
 * Demonstrates: catching specific exceptions and rethrowing as meaningful
 * domain exceptions, preserving the original cause for diagnostics.
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
public class CatchAndRethrow {

    private static final Logger logger =
            Logger.getLogger(CatchAndRethrow.class.getName());

    // Simulated domain exception wrapping a root cause
    static class DomainException extends RuntimeException {
        DomainException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // Simulated checked exceptions for demonstration
    static class SpecificException extends Exception {
        SpecificException(String msg) { super(msg); }
    }

    static class AnotherException extends Exception {
        AnotherException(String msg) { super(msg); }
    }

    // Simulates core logic that may throw checked exceptions
    void performCoreLogic(int scenario) throws SpecificException, AnotherException {
        if (scenario == 1) {
            throw new SpecificException("Resource not found");
        } else if (scenario == 2) {
            throw new AnotherException("Downstream service unavailable");
        }
    }

    // Catches specific exceptions and rethrows as domain exceptions
    void process(int scenario) {
        try {
            performCoreLogic(scenario); // Core logic invocation
        } catch (SpecificException e) {
            // Wrap with contextual message; original cause is preserved
            throw new DomainException("Contextual message: resource lookup failed", e);
        } catch (AnotherException e) {
            // Log before rethrowing so the event is recorded
            logger.log(Level.SEVERE, "An error occurred while processing", e);
            throw new DomainException("Processing failed: downstream error", e);
        }
    }

    void main() {
        CatchAndRethrow handler = new CatchAndRethrow();

        // Scenario 1: SpecificException caught and rethrown as DomainException
        try {
            handler.process(1);
        } catch (DomainException e) {
            logger.log(Level.WARNING, "Caught domain exception: " + e.getMessage()
                    + " | cause: " + e.getCause().getMessage());
        }

        // Scenario 2: AnotherException logged then rethrown as DomainException
        try {
            handler.process(2);
        } catch (DomainException e) {
            logger.log(Level.WARNING, "Caught domain exception: " + e.getMessage()
                    + " | cause: " + e.getCause().getMessage());
        }

        // Scenario 0: No exception thrown; normal flow
        handler.process(0);
        logger.info("Scenario 0 completed without exception");

        // Output:
        // SEVERE: An error occurred while processing
        // WARNING: Caught domain exception: Contextual message: resource lookup failed | cause: Resource not found
        // WARNING: Caught domain exception: Processing failed: downstream error | cause: Downstream service unavailable
        // INFO: Scenario 0 completed without exception
    }
}