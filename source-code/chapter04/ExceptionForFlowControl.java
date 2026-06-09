// Java 8+
/**
 * Listing 4.19 — ExceptionForFlowControl.java
 * Demonstrates: Using exceptions for flow control (anti-pattern)
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 8+
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionForFlowControl {

    private static final Logger logger =
            Logger.getLogger(ExceptionForFlowControl.class.getName());

    /**
     * Anti-pattern: using exception as flow control to parse input.
     * Exceptions should signal unexpected failures, not drive logic.
     */
    static int parseWithException(String input) {
        int value;
        try {
            value = Integer.parseInt(input); // attempt parse
        } catch (NumberFormatException e) {
            value = 0; // silently fall back to default — smell
        }
        return value;
    }

    /**
     * Better approach: validate before parsing to avoid exception-driven flow.
     */
    static int parseWithValidation(String input) {
        if (input == null || !input.matches("-?\\d+")) {
            return 0; // explicit default, no exception needed
        }
        return Integer.parseInt(input); // safe to parse now
    }

    public static void main(String[] args) {
        String validInput   = "42";
        String invalidInput = "abc";
        String nullInput    = null;

        // Anti-pattern: exception used to control flow
        logger.log(Level.INFO, "=== Exception-for-flow-control (anti-pattern) ===");
        logger.log(Level.INFO, "{0}", parseWithException(validInput));   // 42
        logger.log(Level.INFO, "{0}", parseWithException(invalidInput)); // 0
        logger.log(Level.INFO, "{0}", parseWithException(nullInput));    // 0

        // Better: validate first, parse only when safe
        logger.log(Level.INFO, "=== Validate-before-parse (preferred) ===");
        logger.log(Level.INFO, "{0}", parseWithValidation(validInput));   // 42
        logger.log(Level.INFO, "{0}", parseWithValidation(invalidInput)); // 0
        logger.log(Level.INFO, "{0}", parseWithValidation(nullInput));    // 0

        // Output:
        // INFO: === Exception-for-flow-control (anti-pattern) ===
        // INFO: 42
        // INFO: 0
        // INFO: 0
        // INFO: === Validate-before-parse (preferred) ===
        // INFO: 42
        // INFO: 0
        // INFO: 0
    }
}
