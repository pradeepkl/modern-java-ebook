// Java 25+
// Feature shown: pattern matching for switch with sealed exception hierarchies, final in Java 21+

/**
 * Listing 4.9 — PatternMatchingExceptions.java
 * Demonstrates: exhaustive switch pattern matching over a sealed exception hierarchy
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 21+ for pattern matching for switch; Java 25+ for void main()
 */
package chapter04;

import java.util.logging.Logger;

public class PatternMatchingExceptions {

    private static final Logger LOG = Logger.getLogger(PatternMatchingExceptions.class.getName());

    // Sealed base exception — closed set of permitted payment failures
    sealed static class PaymentException extends RuntimeException
            permits InsufficientFundsException, InvalidCardException, PaymentGatewayException {
        PaymentException(String message) { super(message); }
    }

    // Permitted subclass: insufficient account balance
    static final class InsufficientFundsException extends PaymentException {
        InsufficientFundsException(String message) { super(message); }
    }

    // Permitted subclass: card details invalid or expired
    static final class InvalidCardException extends PaymentException {
        InvalidCardException(String message) { super(message); }
    }

    // Permitted subclass: downstream payment gateway unreachable
    static final class PaymentGatewayException extends PaymentException {
        PaymentGatewayException(String message) { super(message); }
    }

    // Simulated payment service — throws one of the sealed subtypes
    static void process(String order) {
        if (order.equals("LOW_BALANCE"))  throw new InsufficientFundsException("Balance too low for order: " + order);
        if (order.equals("BAD_CARD"))     throw new InvalidCardException("Card expired for order: " + order);
        if (order.equals("GATEWAY_DOWN")) throw new PaymentGatewayException("Gateway timeout for order: " + order);
    }

    void handleLowBalance(InsufficientFundsException ex) {
        LOG.info("Low balance handler: " + ex.getMessage());
    }

    void handleInvalidCard(InvalidCardException ex) {
        LOG.info("Invalid card handler: " + ex.getMessage());
    }

    void handleGatewayFailure(PaymentGatewayException ex) {
        LOG.info("Gateway failure handler: " + ex.getMessage());
    }

    void main() {
        String[] orders = {"LOW_BALANCE", "BAD_CARD", "GATEWAY_DOWN"};

        for (String order : orders) {
            try {
                process(order); // throws a sealed PaymentException subtype
            } catch (PaymentException e) {
                // Switch expression forces exhaustiveness; default covers unsealed base type
                switch (e) {
                    case InsufficientFundsException ex -> handleLowBalance(ex);
                    case InvalidCardException       ex -> handleInvalidCard(ex);
                    case PaymentGatewayException    ex -> handleGatewayFailure(ex);
                    default -> LOG.warning("Unhandled payment exception: " + e.getMessage());
                }
            }
        }
        // Output:
        // INFO: Low balance handler: Balance too low for order: LOW_BALANCE
        // INFO: Invalid card handler: Card expired for order: BAD_CARD
        // INFO: Gateway failure handler: Gateway timeout for order: GATEWAY_DOWN
    }
}