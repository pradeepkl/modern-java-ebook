// Java 21+
/**
 * Listing 6.8 — NullInSwitch.java
 * Demonstrates: Handling null as a first-class case in pattern matching switch
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 21+
 */
package chapter06;

import java.util.logging.Logger;

public class NullInSwitch {

    private static final Logger log = Logger.getLogger(NullInSwitch.class.getName());

    // Sealed interface — compiler knows all permitted subtypes
    sealed interface Notification
            permits NullInSwitch.EmailNotification,
                    NullInSwitch.SmsNotification {}

    record EmailNotification(String to,
            String subject,
            String body) implements Notification {}

    record SmsNotification(String phoneNumber,
            String message) implements Notification {}

    // null is now a valid case — no NullPointerException thrown
    public static String describe(Notification notification) {
        return switch (notification) {
            case null ->
                    "No notification provided";           // explicit null handling
            case EmailNotification e ->
                    "Email to " + e.to();                 // type pattern binding
            case SmsNotification s ->
                    "SMS to " + s.phoneNumber();          // type pattern binding
        };
    }

    public static void main(String[] args) {
        EmailNotification email = new EmailNotification(
                "alice@example.com", "Hello", "Body text");
        SmsNotification sms = new SmsNotification(
                "+1-555-0100", "Your code is 1234");

        // Demonstrate each case including null
        log.info(describe(email));   // Email to alice@example.com
        log.info(describe(sms));     // SMS to +1-555-0100
        log.info(describe(null));    // No notification provided

        // Output:
        // INFO: Email to alice@example.com
        // INFO: SMS to +1-555-0100
        // INFO: No notification provided
    }
}