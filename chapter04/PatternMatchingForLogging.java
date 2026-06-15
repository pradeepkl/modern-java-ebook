// Java 25+
// Feature shown: pattern matching for switch with sealed classes, final in Java 21+

/**
 * Listing 4.10 — PatternMatchingForLogging.java
 * Demonstrates: exhaustive switch pattern matching over a sealed exception
 * hierarchy for structured, level-appropriate logging decisions.
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 21+ for pattern matching for switch (final);
 * Java 25+ for compact source files and instance main methods (JEP 512).
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PatternMatchingForLogging {

    // Sealed AppException hierarchy — three permitted subclasses, closed set
    sealed interface AppException permits
            AppException.ValidationException,
            AppException.DatabaseException,
            AppException.PaymentGatewayException {

        String getMessage();

        // Validation failure — caller supplied bad input
        record ValidationException(String getMessage) implements AppException {}

        // Infrastructure failure — database unreachable or query failed
        record DatabaseException(String getMessage) implements AppException {}

        // External payment gateway returned an error
        record PaymentGatewayException(String getMessage) implements AppException {}
    }

    private static final Logger logger =
            Logger.getLogger(PatternMatchingForLogging.class.getName());

    // Logs the exception at the appropriate level based on its sealed type.
    // The compiler verifies exhaustiveness — no default branch is needed.
    void logException(AppException exception) {
        switch (exception) {
            case AppException.ValidationException e ->
                    logger.log(Level.WARNING, e.getMessage());       // user error
            case AppException.DatabaseException e ->
                    logger.log(Level.SEVERE, e.getMessage());        // infra error
            case AppException.PaymentGatewayException e ->
                    logger.log(
                        Level.SEVERE,
                        () -> "Gateway failure: " + e.getMessage()  // lazy supplier
                    );
        }
    }

    void main() {
        var demo = new PatternMatchingForLogging();

        demo.logException(new AppException.ValidationException("Email address is blank"));
        demo.logException(new AppException.DatabaseException("Connection pool exhausted"));
        demo.logException(new AppException.PaymentGatewayException("Timeout after 30s"));

        // Output (log records written to console handler at WARNING / SEVERE):
        // WARNING: Email address is blank
        // SEVERE:  Connection pool exhausted
        // SEVERE:  Gateway failure: Timeout after 30s
    }
}