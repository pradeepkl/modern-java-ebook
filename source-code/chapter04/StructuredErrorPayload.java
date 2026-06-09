// Java 16+
/**
 * Listing 4.11 — StructuredErrorPayload.java
 * Demonstrates: Using records as structured error payloads inside exceptions
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 16+
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Record carries the structured error context — immutable, compact, no boilerplate.
 */
record UserNotFoundDetails(
        Long userId,
        String reason,
        String requestedBy) {
}

/**
 * Exception carries the record as its payload, gaining rich context without ceremony.
 */
final class UserNotFoundException extends RuntimeException {

    private final UserNotFoundDetails details; // structured payload

    public UserNotFoundException(UserNotFoundDetails details) {
        super("User " + details.userId()
                + " not found: " + details.reason());
        this.details = details;
    }

    public UserNotFoundDetails details() {
        return details; // expose the full structured context
    }
}

public class StructuredErrorPayload {

    private static final Logger logger =
            Logger.getLogger(StructuredErrorPayload.class.getName());

    /**
     * Simulates a service method that throws a richly structured exception.
     */
    static void findUser(long userId, String requestedBy) {
        throw new UserNotFoundException(
                new UserNotFoundDetails(
                        userId,
                        "User does not exist",
                        requestedBy
                )
        );
    }

    public static void main(String[] args) {
        try {
            findUser(12345L, "order-service"); // trigger the structured exception
        } catch (UserNotFoundException e) {
            UserNotFoundDetails d = e.details(); // extract the record payload
            logger.log(Level.WARNING, "Exception message : {0}", e.getMessage());
            logger.log(Level.INFO, "User ID           : {0}", d.userId());
            logger.log(Level.INFO, "Reason            : {0}", d.reason());
            logger.log(Level.INFO, "Requested by      : {0}", d.requestedBy());
        }
        // Output:
        // WARNING: Exception message : User 12345 not found: User does not exist
        // INFO: User ID           : 12345
        // INFO: Reason            : User does not exist
        // INFO: Requested by      : order-service
    }
}
