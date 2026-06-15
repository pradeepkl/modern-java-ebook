// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 4.20 — ExceptionForFlowControl.java
 * Demonstrates: Smell 3 — Exception Used for Flow Control
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionForFlowControl {

    private static final Logger logger =
            Logger.getLogger(ExceptionForFlowControl.class.getName());

    // Smell: using exception as a control-flow mechanism
    static int parseWithException(String input) {
        int value;
        try {
            value = Integer.parseInt(input); // throws if not a valid integer
        } catch (NumberFormatException e) {
            value = 0; // silently fall back to default — hides the real problem
        }
        return value;
    }

    // Preferred: validate before parsing, no exception needed for normal flow
    static int parseWithValidation(String input) {
        if (input == null || !input.matches("-?\\d+")) {
            logger.log(Level.WARNING,
                    "Invalid numeric input ''{0}'', using default 0", input);
            return 0; // explicit default, no exception thrown or caught
        }
        return Integer.parseInt(input);
    }

    void main() {
        String validInput   = "42";
        String invalidInput = "abc";

        // Smell pattern: exception drives the default-value branch
        int v1 = parseWithException(validInput);
        int v2 = parseWithException(invalidInput);
        logger.log(Level.INFO, "Smell pattern — valid: {0}, invalid: {1}",
                new Object[]{v1, v2});

        // Preferred pattern: guard clause avoids exception for expected input
        int v3 = parseWithValidation(validInput);
        int v4 = parseWithValidation(invalidInput);
        logger.log(Level.INFO, "Preferred pattern — valid: {0}, invalid: {1}",
                new Object[]{v3, v4});

        // Output:
        // WARNING: Invalid numeric input 'abc', using default 0
        // INFO: Smell pattern   -- valid: 42, invalid: 0
        // INFO: Preferred pattern -- valid: 42, invalid: 0
    }
}