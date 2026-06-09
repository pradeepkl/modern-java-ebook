// Java 21+
/**
 * Listing 4.9 — PatternMatchingExceptions.java
 * Demonstrates: Pattern matching with sealed exception hierarchies in switch expressions
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 21+
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PatternMatchingExceptions {

    private static final Logger logger =
            Logger.getLogger(PatternMatchingExceptions.class.getName());

    // Sealed exception hierarchy — closed set of permitted subclasses
    sealed static class PaymentException extends RuntimeException
            permits InsufficientFundsException, InvalidCardException, PaymentGatewayException {
        public PaymentException(String message) { super(message); }
    }

    static final class InsufficientFundsException extends PaymentException {
        public InsufficientFundsException(String message) { super(message); }
    }

    static final class InvalidCardException extends PaymentException {
        public InvalidCardException(String message) { super(message); }
    }

    static final class PaymentGatewayException extends PaymentException {
        public PaymentGatewayException(String message) { super(message); }
    }

    // Simulated payment service that throws one of the sealed subtypes
    static class PaymentService {
        void process(String order, PaymentException toThrow) {
            throw toThrow;
        }
    }

    static void handleLowBalance(InsufficientFundsException ex) {
        logger.log(Level.WARNING, "Low balance: {0}", ex.getMessage());
    }

    static void handleInvalidCard(InvalidCardException ex) {
        logger.log(Level.WARNING, "Invalid card: {0}", ex.getMessage());
    }

    static void handleGatewayFailure(PaymentGatewayException ex) {
        logger.log(Level.SEVERE, "Gateway failure: {0}", ex.getMessage());
    }

    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService();

        PaymentException[] scenarios = {
            new InsufficientFundsException("Account balance too low"),
            new InvalidCardException("Card number failed Luhn check"),
            new PaymentGatewayException("Stripe API timed out")
        };

        for (PaymentException scenario : scenarios) {
            try {
                paymentService.process("order-42", scenario);
            } catch (PaymentException e) {
                // Exhaustive pattern matching — compiler verifies all sealed subtypes handled
                switch (e) {
                    case InsufficientFundsException ex -> handleLowBalance(ex);
                    case InvalidCardException ex       -> handleInvalidCard(ex);
                    case PaymentGatewayException ex    -> handleGatewayFailure(ex);
                    default -> throw new AssertionError("Unexpected subtype: " + e);
                }
            }
        }
        // Output:
        // WARNING: Low balance: Account balance too low
        // WARNING: Invalid card: Card number failed Luhn check
        // SEVERE: Gateway failure: Stripe API timed out
    }
}
