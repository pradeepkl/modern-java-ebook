// Java 25+
// Feature shown: pattern matching for switch, final in Java 21+
/**
 * Listing 6.7 — SwitchPatternMatching.java
 * Demonstrates: pattern matching in switch with sealed interfaces
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 21+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter06;

import java.util.logging.Logger;

public class SwitchPatternMatching {

    private static final Logger log =
            Logger.getLogger(SwitchPatternMatching.class.getName());

    // Sealed interface — compiler knows all permitted subtypes
    sealed interface Notification
            permits EmailNotification, SmsNotification, PushNotification {}

    record EmailNotification(String to,
            String subject,
            String body) implements Notification {}

    record SmsNotification(String phoneNumber,
            String message) implements Notification {}

    record PushNotification(String deviceToken,
            String title,
            String payload) implements Notification {}

    // Each case matches a type and binds a variable — no explicit cast needed
    public static String describe(Notification notification) {
        return switch (notification) {
            case EmailNotification e ->
                    "Email to " + e.to() + ": " + e.subject();
            case SmsNotification s ->
                    "SMS to " + s.phoneNumber();
            case PushNotification p ->
                    "Push to " + p.deviceToken() + ": " + p.title();
            // No default — Notification is sealed.
            // The compiler knows all three permitted subtypes.
            // Every case is verified at compile time.
        };
    }

    void main() {
        Notification email = new EmailNotification(
                "alice@example.com", "Welcome", "Hello Alice");
        Notification sms = new SmsNotification(
                "+15550001234", "Your code is 9876");
        Notification push = new PushNotification(
                "token-abc-123", "New Message", "{\"msg\":\"Hi\"}");

        log.info(describe(email)); // Email to alice@example.com: Welcome
        log.info(describe(sms));   // SMS to +15550001234
        log.info(describe(push));  // Push to token-abc-123: New Message

        // Output:
        // INFO: Email to alice@example.com: Welcome
        // INFO: SMS to +15550001234
        // INFO: Push to token-abc-123: New Message
    }
}