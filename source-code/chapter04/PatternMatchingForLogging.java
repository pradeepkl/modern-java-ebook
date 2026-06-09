// Java 21+
/**
 * Listing 4.10 — PatternMatchingForLogging.java
 * Demonstrates: Pattern matching switch for exception-based logging decisions
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 21+
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

// Sealed exception hierarchy for domain failures
sealed class AppException extends RuntimeException
        permits AppException.ValidationException,
                AppException.DatabaseException,
                AppException.PaymentGatewayException {
    public AppException(String message) { super(message); }

    static final class ValidationException extends AppException {
        public ValidationException(String msg) { super(msg); }
    }

    static final class DatabaseException extends AppException {
        public DatabaseException(String msg) { super(msg); }
    }

    static final class PaymentGatewayException extends AppException {
        public PaymentGatewayException(String msg) { super(msg); }
    }
}

public class PatternMatchingForLogging {

    private static final Logger logger =
            Logger.getLogger(PatternMatchingForLogging.class.getName());

    // Route exception to appropriate log level using pattern matching switch
    static void logException(Exception exception) {
        switch (exception) {
            case AppException.ValidationException e ->
                    logger.warning(e.getMessage());           // WARNING for validation
            case AppException.DatabaseException e ->
                    logger.severe(e.getMessage());            // SEVERE for DB errors
            case AppException.PaymentGatewayException e ->
                    logger.log(
                        Level.SEVERE,
                        () -> "Gateway failure: " + e.getMessage() // lazy supplier
                    );
            default ->
                    logger.info(
                        "Unclassified exception: "
                        + exception.getClass().getSimpleName()     // fallback case
                    );
        }
    }

    public static void main(String[] args) {
        logException(new AppException.ValidationException("Invalid email format"));
        logException(new AppException.DatabaseException("Connection timeout"));
        logException(new AppException.PaymentGatewayException("Stripe unreachable"));
        logException(new RuntimeException("Something unexpected"));
        // Output:
        // WARNING: Invalid email format
        // SEVERE: Connection timeout
        // SEVERE: Gateway failure: Stripe unreachable
        // INFO: Unclassified exception: RuntimeException
    }
}