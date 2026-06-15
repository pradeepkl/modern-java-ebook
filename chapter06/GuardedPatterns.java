// Java 25+
// Feature shown: guarded patterns with when clause in switch, final in Java 21+

/**
 * Listing 6.10 — GuardedPatterns.java
 * Demonstrates: guarded patterns combining type match and when condition
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 21+ for pattern matching for switch (final in Java 21+);
 * void main() instance main method requires Java 25+ (JEP 512)
 */
package chapter06;

import java.util.logging.Logger;

public class GuardedPatterns {

    private static final Logger log =
            Logger.getLogger(GuardedPatterns.class.getName());

    // sealed CLASS — not interface — because it extends RuntimeException
    public static sealed class PaymentException
            extends RuntimeException
            permits InsufficientFundsException,
                    InvalidCardException,
                    PaymentGatewayException {
        public PaymentException(String message) { super(message); }
    }

    // final class — records cannot extend arbitrary classes
    public static final class InsufficientFundsException
            extends PaymentException {
        private final double deficit;
        public InsufficientFundsException(double deficit) {
            super("Insufficient funds: deficit=" + deficit);
            this.deficit = deficit;
        }
        public double deficit() { return deficit; }
    }

    public static final class InvalidCardException extends PaymentException {
        public InvalidCardException() { super("Invalid card"); }
    }

    public static final class PaymentGatewayException extends PaymentException {
        public PaymentGatewayException() { super("Payment gateway failure"); }
    }

    public static void handle(PaymentException e) {
        // default arm satisfies exhaustiveness; sealed hierarchy covers all known types
        switch (e) {
            // Guarded: type match AND condition — large deficits escalated
            case InsufficientFundsException ex when ex.deficit() > 1000 ->
                log.severe("Large deficit - escalating: " + ex.deficit());
            // Unguarded arm handles remaining InsufficientFundsException cases
            case InsufficientFundsException ex ->
                log.warning("Small deficit - retry: " + ex.deficit());
            case InvalidCardException ex ->
                log.warning("Invalid card - ask customer");
            case PaymentGatewayException ex ->
                log.severe("Gateway failure - notify ops");
            // default required: guarded arm leaves switch non-exhaustive
            default ->
                log.warning("Unknown payment exception: " + e.getMessage());
        }
    }

    void main() {
        handle(new InsufficientFundsException(1500.00)); // large deficit
        handle(new InsufficientFundsException(50.00));   // small deficit
        handle(new InvalidCardException());
        handle(new PaymentGatewayException());
        // Output:
        // SEVERE: Large deficit - escalating: 1500.0
        // WARNING: Small deficit - retry: 50.0
        // WARNING: Invalid card - ask customer
        // SEVERE: Gateway failure - notify ops
    }
}