// Java 17+
/**
 * Listing 4.8 — SealedPaymentException.java
 * Demonstrates: Sealed exception hierarchies with permitted subclasses
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 17+
 */
package chapter04;

// Sealed base exception — only the listed subclasses are permitted
sealed class PaymentException extends RuntimeException
        permits InsufficientFundsException,
                InvalidCardException,
                PaymentGatewayException {
    public PaymentException(String message) {
        super(message);
    }
}

// Each subclass is final — no further extension allowed
final class InsufficientFundsException extends PaymentException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

final class InvalidCardException extends PaymentException {
    public InvalidCardException(String message) {
        super(message);
    }
}

final class PaymentGatewayException extends PaymentException {
    public PaymentGatewayException(String message) {
        super(message);
    }
}

public class SealedPaymentException {

    // Pattern matching switch — exhaustive over sealed type via guarded patterns
    static String describe(PaymentException ex) {
        return switch (ex) {
            case InsufficientFundsException e ->
                    "Insufficient funds: " + e.getMessage();
            case InvalidCardException e ->
                    "Invalid card: " + e.getMessage();
            case PaymentGatewayException e ->
                    "Gateway error: " + e.getMessage();
            // Default required when sealed class is in a different compilation unit
            default -> "Unknown payment error: " + ex.getMessage();
        };
    }

    public static void main(String[] args) {
        PaymentException[] failures = {
            new InsufficientFundsException("Balance too low"),
            new InvalidCardException("Card number invalid"),
            new PaymentGatewayException("Timeout connecting to gateway")
        };

        for (PaymentException ex : failures) {
            System.out.println(describe(ex)); // Each subtype handled explicitly
        }

        // Output:
        // Insufficient funds: Balance too low
        // Invalid card: Card number invalid
        // Gateway error: Timeout connecting to gateway
    }
}