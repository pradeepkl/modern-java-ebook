// Java 8+
/**
 * Listing 4.19 — ExceptionForFlowControl.java
 * Demonstrates: Using exceptions for flow control (anti-pattern)
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 8+
 */
package chapter04;

public class ExceptionForFlowControl {

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
        System.out.println("=== Exception-for-flow-control (anti-pattern) ===");
        System.out.println(parseWithException(validInput));   // 42
        System.out.println(parseWithException(invalidInput)); // 0
        System.out.println(parseWithException(nullInput));    // 0

        // Better: validate first, parse only when safe
        System.out.println("=== Validate-before-parse (preferred) ===");
        System.out.println(parseWithValidation(validInput));   // 42
        System.out.println(parseWithValidation(invalidInput)); // 0
        System.out.println(parseWithValidation(nullInput));    // 0

        // Output:
        // === Exception-for-flow-control (anti-pattern) ===
        // 42
        // 0
        // 0
        // === Validate-before-parse (preferred) ===
        // 42
        // 0
        // 0
    }
}