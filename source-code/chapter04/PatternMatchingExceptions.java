// Java 21+
/**
 * Listing 4.9 — PatternMatchingExceptions.java
 * Demonstrates: Pattern matching with sealed exception hierarchies in switch expressions
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 21+
 */
package chapter04;

public class PatternMatchingExceptions {

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
        System.out.println("Low balance: " + ex.getMessage());
    }

    static void handleInvalidCard(InvalidCardException ex) {
        System.out.println("Invalid card: " + ex.getMessage());
    }

    static void handleGatewayFailure(PaymentGatewayException ex) {
        System.out.println("Gateway failure: " + ex.getMessage());
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
        // Low balance: Account balance too low
        // Invalid card: Card number failed Luhn check
        // Gateway failure: Stripe API timed out
    }
}