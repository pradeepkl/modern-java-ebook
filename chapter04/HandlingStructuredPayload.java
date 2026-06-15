// Java 25+
// Feature shown: records as structured exception payloads, final in Java 16+

/**
 * Listing 4.12 — HandlingStructuredPayload.java
 * Demonstrates: Catching an exception that carries a record payload and
 * accessing typed fields from the structured error context.
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

// Record carries the structured error context
record UserNotFoundDetails(Long userId, String reason, String requestedBy) {}

// Exception carries the record as its payload
final class UserNotFoundException extends RuntimeException {
    private final UserNotFoundDetails details;

    UserNotFoundException(UserNotFoundDetails details) {
        super("User " + details.userId() + " not found: " + details.reason());
        this.details = details;
    }

    UserNotFoundDetails details() {
        return details;
    }
}

// Simulated service that throws the structured exception
class UserService {
    void load(Long userId) {
        throw new UserNotFoundException(
            new UserNotFoundDetails(userId, "User does not exist", "order-service")
        );
    }
}

public class HandlingStructuredPayload {

    private static final Logger logger =
        Logger.getLogger(HandlingStructuredPayload.class.getName());

    void main() {
        var userService = new UserService();
        Long userId = 12345L;

        try {
            userService.load(userId); // throws UserNotFoundException
        } catch (UserNotFoundException e) {
            var details = e.details(); // access the record payload
            logger.log(Level.WARNING, () ->
                "User " + details.userId()          // typed Long field
                + " not found. Requested by: "
                + details.requestedBy()             // typed String field
            );
            // details.reason() also available as a typed field
            logger.log(Level.FINE, () -> "Reason: " + details.reason());
        }

        // Output:
        // WARNING: User 12345 not found. Requested by: order-service
        // FINE:    Reason: User does not exist
    }
}