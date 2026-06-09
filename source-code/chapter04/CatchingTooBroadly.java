// Java 8+
/**
 * Listing 4.18 — CatchingTooBroadly.java
 * Demonstrates: The smell of catching Exception too broadly,
 *               masking specific failure types and losing diagnostic precision.
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 8+
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CatchingTooBroadly {

    private static final Logger logger =
            Logger.getLogger(CatchingTooBroadly.class.getName());

    // Simulated order type
    record Order(int id, String item) {}

    // Throws different exceptions depending on order state
    static void processOrder(Order order) throws Exception {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }
        if (order.id() <= 0) {
            throw new IllegalStateException("Order ID is invalid: " + order.id());
        }
        if (order.item() == null || order.item().isBlank()) {
            throw new Exception("Item description is missing");
        }
        logger.log(Level.INFO, "Order processed: {0}", order);
    }

    public static void main(String[] args) {

        Order[] orders = {
            new Order(1, "Widget"),   // valid
            new Order(-1, "Gadget"),  // invalid ID
            null                      // null order
        };

        for (Order order : orders) {
            try {
                processOrder(order);
            } catch (Exception e) {
                // TOO BROAD: catches IllegalArgumentException, IllegalStateException,
                // and checked Exception all the same way — loses diagnostic precision
                logger.log(Level.SEVERE, "Something went wrong", e);
            }
        }

        logger.log(Level.INFO, "--- Better: catch specific types ---");

        for (Order order : orders) {
            try {
                processOrder(order);
            } catch (IllegalArgumentException e) {
                logger.log(Level.WARNING, "Bad input: {0}", e.getMessage());
            } catch (IllegalStateException e) {
                logger.log(Level.WARNING, "Invalid state: {0}", e.getMessage());
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Unexpected failure", e);
            }
        }

        // Output:
        // INFO: Order processed: Order[id=1, item=Widget]
        // SEVERE: Something went wrong (IllegalStateException: Order ID is invalid: -1)
        // SEVERE: Something went wrong (IllegalArgumentException: Order must not be null)
        // INFO: --- Better: catch specific types ---
        // INFO: Order processed: Order[id=1, item=Widget]
        // WARNING: Bad input: Order must not be null
        // WARNING: Invalid state: Order ID is invalid: -1
    }
}