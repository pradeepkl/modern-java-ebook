// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 4.19 — CatchingTooBroadly.java
 * Demonstrates: Smell 2 — Catching Too Broadly, where a broad Exception
 * catch masks specific failure types and loses diagnostic precision.
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CatchingTooBroadly {

    private static final Logger logger =
            Logger.getLogger(CatchingTooBroadly.class.getName());

    // Simulated order type for demonstration
    record Order(String id, double amount) {}

    // Simulates order processing that may throw specific exceptions
    static void processOrder(Order order) throws IllegalArgumentException {
        if (order == null) {
            throw new NullPointerException("Order must not be null");
        }
        if (order.amount() <= 0) {
            throw new IllegalArgumentException(
                    "Order amount must be positive: " + order.amount());
        }
        logger.info("Order processed: " + order.id());
    }

    void main() {
        Order order = new Order("ORD-001", -5.0); // invalid amount

        // Smell: catching Exception too broadly hides the specific failure type.
        // A NullPointerException and an IllegalArgumentException are treated
        // identically, making root-cause analysis harder.
        try {
            processOrder(order);
        } catch (Exception e) {
            // Logs the exception but loses the specific type distinction
            logger.log(
                    Level.SEVERE,
                    "Something went wrong",
                    e);
        }

        // Preferred alternative: catch specific exception types
        try {
            processOrder(null);
        } catch (NullPointerException e) {
            logger.log(Level.SEVERE, "Order reference was null", e);
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Order data was invalid", e);
        }

        // Output:
        // SEVERE: Something went wrong (IllegalArgumentException)
        // SEVERE: Order reference was null (NullPointerException)
    }
}