// Java 25+
// Feature shown: instanceof pattern matching, final in Java 16+
/**
 * Listing 6.2 — InstanceofPatternMatching.java
 * Demonstrates: instanceof pattern matching — check and bind in one expression,
 * eliminating the redundant cast required by traditional type dispatch.
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 16+ for instanceof pattern matching; Java 17+ for sealed
 * interfaces; Java 25+ for the void main() instance main method (JEP 512).
 */
package chapter06;

import java.util.logging.Logger;

public class InstanceofPatternMatching {

    private static final Logger log =
            Logger.getLogger(InstanceofPatternMatching.class.getName());

    // Sealed hierarchy — compiler knows every permitted subtype
    sealed interface Notification
            permits EmailNotification, SmsNotification, PushNotification {}

    record EmailNotification(String to, String subject, String body)
            implements Notification {}

    record SmsNotification(String phoneNumber, String message)
            implements Notification {}

    record PushNotification(String deviceToken, String title, String payload)
            implements Notification {}

    /**
     * Check and bind in one expression — no explicit cast required.
     * The pattern variable (e, s, p) is in scope only inside its branch.
     */
    public static String describe(Notification notification) {
        if (notification instanceof EmailNotification e) {       // bind to e
            return "Email to " + e.to() + ": " + e.subject();
        } else if (notification instanceof SmsNotification s) {  // bind to s
            return "SMS to " + s.phoneNumber();
        } else if (notification instanceof PushNotification p) { // bind to p
            return "Push to " + p.deviceToken() + ": " + p.title();
        }
        throw new IllegalArgumentException("Unknown: " + notification);
    }

    void main() {
        Notification email = new EmailNotification(
                "alice@example.com", "Welcome", "Hello Alice");
        Notification sms   = new SmsNotification("+15550001234", "Your code is 9876");
        Notification push  = new PushNotification("tok-abc123", "Sale", "50% off today");

        log.info(describe(email)); // pattern variable e used directly
        log.info(describe(sms));   // pattern variable s used directly
        log.info(describe(push));  // pattern variable p used directly

        // Output:
        // INFO: Email to alice@example.com: Welcome
        // INFO: SMS to +15550001234
        // INFO: Push to tok-abc123: Sale
    }
}