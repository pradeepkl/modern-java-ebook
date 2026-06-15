// Java 25+
// Feature shown: sealed classes and interfaces, final in Java 17+

/**
 * Listing 4.8 — SealedPaymentException.java
 * Demonstrates: sealed exception hierarchies with a closed set of permitted subclasses
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 17+ for sealed classes; Java 25+ for void main() instance main method
 */
package chapter04;

import java.util.logging.Logger;

public sealed class SealedPaymentException extends RuntimeException
        permits SealedPaymentException.InsufficientFundsException,
                SealedPaymentException.InvalidCardException,
                SealedPaymentException.PaymentGatewayException {

    private static final Logger LOG =
            Logger.getLogger(SealedPaymentException.class.getName());

    protected SealedPaymentException(String message) {
        super(message);
    }

    // All permitted subclasses declared as nested finals for single-file compilation
    public static final class InsufficientFundsException
            extends SealedPaymentException {
        public InsufficientFundsException(String message) { super(message); }
    }

    public static final class InvalidCardException
            extends SealedPaymentException {
        public InvalidCardException(String message) { super(message); }
    }

    public static final class PaymentGatewayException
            extends SealedPaymentException {
        public PaymentGatewayException(String message) { super(message); }
    }

    // Pattern matching for switch exhaustively covers all permitted subtypes
    static String describe(SealedPaymentException ex) {
        // Cast to Object so the compiler applies pattern matching exhaustiveness rules
        return switch ((Object) ex) {
            case InsufficientFundsException e -> "Insufficient funds: " + e.getMessage();
            case InvalidCardException e       -> "Invalid card: "       + e.getMessage();
            case PaymentGatewayException e    -> "Gateway error: "      + e.getMessage();
            default -> throw new AssertionError("Unknown subtype: " + ex.getClass());
        };
    }

    void main() {
        SealedPaymentException[] failures = {
            new InsufficientFundsException("Balance too low"),
            new InvalidCardException("Card number invalid"),
            new PaymentGatewayException("Timeout connecting to gateway")
        };

        for (SealedPaymentException ex : failures) {
            LOG.info(describe(ex));                            // closed hierarchy — all cases handled
        }
        // Output:
        // INFO: Insufficient funds: Balance too low
        // INFO: Invalid card: Card number invalid
        // INFO: Gateway error: Timeout connecting to gateway
    }
}