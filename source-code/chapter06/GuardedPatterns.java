// Java 21+
/**
 * Listing 6.10 — GuardedPatterns.java
 * Demonstrates: Guarded patterns combining type match with 'when' conditions
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 21+
 */
package chapter06;

import java.util.logging.Logger;

public class GuardedPatterns {

    private static final Logger log =
            Logger.getLogger(GuardedPatterns.class.getName());

    // Sealed class — extends RuntimeException; interfaces cannot extend classes
    public static sealed class PaymentException extends RuntimeException
            permits InsufficientFundsException, InvalidCardException, PaymentGatewayException {
        public PaymentException(String message) { super(message); }
    }

    // final class — records cannot extend arbitrary classes
    public static final class InsufficientFundsException extends PaymentException {
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
        // Switch over sealed type — compiler verifies exhaustiveness via permits
        switch (e) {
            // Guarded: type match AND condition — large deficits escalated
            case InsufficientFundsException ex when ex.deficit() > 1000 ->
                log.severe("Large deficit — escalating: " + ex.deficit());
            // Same type, no guard — small deficits retried
            case InsufficientFundsException ex ->
                log.warning("Small deficit — retry: " + ex.deficit());
            case InvalidCardException ex ->
                log.warning("Invalid card — ask customer");
            case PaymentGatewayException ex ->
                log.severe("Gateway failure — notify ops");
            // default required: compiler cannot prove exhaustiveness for class hierarchies
            default ->
                log.info("Unhandled payment exception: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        handle(new InsufficientFundsException(1500.00)); // large deficit
        handle(new InsufficientFundsException(50.00));   // small deficit
        handle(new InvalidCardException());               // invalid card
        handle(new PaymentGatewayException());            // gateway failure

        // Output:
        // SEVERE: Large deficit — escalating: 1500.0
        // WARNING: Small deficit — retry: 50.0
        // WARNING: Invalid card — ask customer
        // SEVERE: Gateway failure — notify ops
    }
}