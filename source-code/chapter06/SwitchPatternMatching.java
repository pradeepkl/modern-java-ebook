// Java 21+
/**
 * Listing 6.7 — SwitchPatternMatching.java
 * Demonstrates: Switch pattern matching with sealed interfaces
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 21+
 */
package chapter06;

import java.util.logging.Logger;

public class SwitchPatternMatching {

    private static final Logger log =
            Logger.getLogger(SwitchPatternMatching.class.getName());

    // Sealed interface — compiler knows all permitted subtypes
    sealed interface Notification
            permits EmailNotification, SmsNotification, PushNotification {}

    // Each record is a permitted subtype of Notification
    record EmailNotification(String to,
            String subject,
            String body) implements Notification {}

    record SmsNotification(String phoneNumber,
            String message) implements Notification {}

    record PushNotification(String deviceToken,
            String title,
            String payload) implements Notification {}

    public static String describe(Notification notification) {
        // Switch on a sealed type — no default needed
        // Compiler verifies all permitted subtypes are covered
        return switch (notification) {
            case EmailNotification e ->
                    "Email to " + e.to() + ": " + e.subject();
            case SmsNotification s ->
                    "SMS to " + s.phoneNumber();
            case PushNotification p ->
                    "Push to " + p.deviceToken() + ": " + p.title();
        };
        // No default — sealed hierarchy is exhaustively matched
    }

    public static void main(String[] args) {
        // Create one instance of each permitted subtype
        Notification email = new EmailNotification(
                "alice@example.com", "Welcome", "Hello Alice!");
        Notification sms = new SmsNotification(
                "+15551234567", "Your code is 9876");
        Notification push = new PushNotification(
                "device-token-abc", "New Message", "{\"msg\":\"Hi\"}");

        // Each arm binds the matched type without an explicit cast
        log.info(describe(email));
        log.info(describe(sms));
        log.info(describe(push));

        // Output:
        // INFO: Email to alice@example.com: Welcome
        // INFO: SMS to +15551234567
        // INFO: Push to device-token-abc: New Message
    }
}