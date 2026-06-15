// Java 25+
// Feature shown: pattern matching for switch with null case, final in Java 21+

/**
 * Listing 6.8 — NullInSwitch.java
 * Demonstrates: handling null explicitly in a pattern-matching switch expression
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 21+ for pattern matching for switch; void main() final in Java 25+
 */
package chapter06;

import java.util.logging.Logger;

public class NullInSwitch {

    private static final Logger log = Logger.getLogger(NullInSwitch.class.getName());

    // Sealed interface — compiler knows every permitted subtype
    sealed interface Notification
            permits EmailNotification, SmsNotification {}

    record EmailNotification(String to,
            String subject,
            String body) implements Notification {}

    record SmsNotification(String phoneNumber,
            String message) implements Notification {}

    // null is now a first-class case; no NullPointerException thrown
    static String describe(Notification notification) {
        return switch (notification) {
            case null ->
                    "No notification provided";          // explicit null arm
            case EmailNotification e ->
                    "Email to " + e.to();                // type pattern arm
            case SmsNotification s ->
                    "SMS to " + s.phoneNumber();         // type pattern arm
        };
    }

    void main() {
        Notification email = new EmailNotification(
                "alice@example.com", "Welcome", "Hello Alice");
        Notification sms   = new SmsNotification(
                "+15550001234", "Your code is 9876");

        log.info(describe(email));   // Email to alice@example.com
        log.info(describe(sms));     // SMS to +15550001234
        log.info(describe(null));    // No notification provided

        // Output:
        // INFO: Email to alice@example.com
        // INFO: SMS to +15550001234
        // INFO: No notification provided
    }
}