// Java 25+
// Feature shown: records as structured error payloads, final in Java 16+

/**
 * Listing 4.11 — StructuredErrorPayload.java
 * Demonstrates: using a record as a structured error payload inside an exception
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter04;

import java.util.logging.Level;
import java.util.logging.Logger;

// Record carries the structured error context — no boilerplate needed
record UserNotFoundDetails(
        Long userId,
        String reason,
        String requestedBy) {
}

// Exception carries the record as its payload
final class UserNotFoundException extends RuntimeException {

    private final UserNotFoundDetails details;

    UserNotFoundException(UserNotFoundDetails details) {
        super("User " + details.userId()
                + " not found: " + details.reason()); // message built from record
        this.details = details;
    }

    UserNotFoundDetails details() {
        return details; // structured payload accessible to callers
    }
}

public class StructuredErrorPayload {

    private static final Logger logger =
            Logger.getLogger(StructuredErrorPayload.class.getName());

    // Simulates a service method that throws the structured exception
    static void findUser(long userId, String requestedBy) {
        throw new UserNotFoundException(
                new UserNotFoundDetails(
                        userId,
                        "User does not exist",
                        requestedBy
                )
        );
    }

    void main() {
        try {
            findUser(12345L, "order-service"); // triggers the exception
        } catch (UserNotFoundException ex) {
            UserNotFoundDetails d = ex.details(); // extract structured payload
            logger.log(Level.WARNING,
                    "Caught UserNotFoundException: userId={0}, reason={1}, requestedBy={2}",
                    new Object[]{d.userId(), d.reason(), d.requestedBy()});
        }
        // Output:
        // WARNING: Caught UserNotFoundException: userId=12345, reason=User does not exist, requestedBy=order-service
    }
}