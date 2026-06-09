// Java 16+
/**
 * Listing 4.12 — HandlingStructuredPayload.java
 * Demonstrates: Catching exceptions with structured record payloads
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 16+
 */
package chapter04;

import java.util.logging.Logger;

// Record carries the structured error context
record UserNotFoundDetails(Long userId, String reason, String requestedBy) {}

// Exception carries the record as its payload
final class UserNotFoundException extends RuntimeException {
    private final UserNotFoundDetails details;

    public UserNotFoundException(UserNotFoundDetails details) {
        super("User " + details.userId() + " not found: " + details.reason());
        this.details = details;
    }

    public UserNotFoundDetails details() {
        return details;
    }
}

// Simulated service that throws structured exception
class UserService {
    public void load(Long userId) {
        throw new UserNotFoundException(
            new UserNotFoundDetails(userId, "User does not exist", "order-service")
        );
    }
}

public class HandlingStructuredPayload {

    private static final Logger logger =
        Logger.getLogger(HandlingStructuredPayload.class.getName());

    public static void main(String[] args) {
        UserService userService = new UserService();
        Long userId = 12345L;

        try {
            userService.load(userId); // throws UserNotFoundException
        } catch (UserNotFoundException e) {
            var details = e.details(); // extract typed record payload
            logger.warning(() ->
                "User " + details.userId()           // typed field access
                + " not found. Requested by: "
                + details.requestedBy()              // typed field access
            );
            // details.userId(), details.reason() available as typed fields
            System.out.println("userId    : " + details.userId());
            System.out.println("reason    : " + details.reason());
            System.out.println("requestedBy: " + details.requestedBy());
        }

        // Output:
        // userId    : 12345
        // reason    : User does not exist
        // requestedBy: order-service
    }
}