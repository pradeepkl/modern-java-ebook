// Java 17+
/**
 * Listing 6.2 — InstanceofPatternMatching.java
 * Demonstrates: instanceof pattern matching — check and bind in one expression
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 17+
 */
package chapter06;

import java.util.logging.Logger;

public class InstanceofPatternMatching {

    private static final Logger log =
            Logger.getLogger(InstanceofPatternMatching.class.getName());

    // Sealed interface restricts permitted subtypes to three known records
    sealed interface Notification
            permits EmailNotification, SmsNotification, PushNotification {}

    record EmailNotification(String to, String subject, String body)
            implements Notification {}

    record SmsNotification(String phoneNumber, String message)
            implements Notification {}

    record PushNotification(String deviceToken, String title, String payload)
            implements Notification {}

    /**
     * Describes a notification using instanceof pattern matching.
     * The binding variable is declared at the point of the check — no explicit cast needed.
     */
    public static String describe(Notification notification) {
        // Check and bind in one expression — no explicit cast required
        if (notification instanceof EmailNotification e) {
            return "Email to " + e.to() + ": " + e.subject();
        } else if (notification instanceof SmsNotification s) {
            return "SMS to " + s.phoneNumber();
        } else if (notification instanceof PushNotification p) {
            return "Push to " + p.deviceToken() + ": " + p.title();
        }
        throw new IllegalArgumentException("Unknown: " + notification);
    }

    public static void main(String[] args) {
        // Demonstrate each notification type
        Notification email = new EmailNotification(
                "alice@example.com", "Welcome", "Hello Alice");
        Notification sms = new SmsNotification("+15550001234", "Your code is 9876");
        Notification push = new PushNotification(
                "device-token-xyz", "New Message", "{\"msg\":\"Hi\"}");

        log.info(describe(email)); // pattern binds to EmailNotification
        log.info(describe(sms));   // pattern binds to SmsNotification
        log.info(describe(push));  // pattern binds to PushNotification

        // Output:
        // INFO: Email to alice@example.com: Welcome
        // INFO: SMS to +15550001234
        // INFO: Push to device-token-xyz: New Message
    }
}