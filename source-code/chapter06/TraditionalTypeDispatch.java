// Java 17+
/**
 * Listing 6.1 — TraditionalTypeDispatch.java
 * Demonstrates: Traditional instanceof-check-cast-use pattern before pattern matching
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 17+
 */
package chapter06;

import java.util.logging.Logger;

public class TraditionalTypeDispatch {

    private static final Logger LOG =
            Logger.getLogger(TraditionalTypeDispatch.class.getName());

    // Sealed hierarchy — compiler knows every permitted subtype
    sealed interface Notification
            permits EmailNotification, SmsNotification, PushNotification {}

    record EmailNotification(String to, String subject, String body)
            implements Notification {}

    record SmsNotification(String phoneNumber, String message)
            implements Notification {}

    record PushNotification(String deviceToken, String title, String payload)
            implements Notification {}

    /** Three-step ceremony: check, cast, use — repeated for every branch. */
    public static String describe(Notification notification) {
        if (notification instanceof EmailNotification) {
            EmailNotification e = (EmailNotification) notification; // redundant cast
            return "Email to " + e.to() + ": " + e.subject();
        } else if (notification instanceof SmsNotification) {
            SmsNotification s = (SmsNotification) notification;     // redundant cast
            return "SMS to " + s.phoneNumber();
        } else if (notification instanceof PushNotification) {
            PushNotification p = (PushNotification) notification;   // redundant cast
            return "Push to " + p.deviceToken() + ": " + p.title();
        }
        // Compiler cannot verify exhaustiveness — this line is reachable at runtime
        throw new IllegalArgumentException("Unknown notification: " + notification);
    }

    public static void main(String[] args) {
        Notification email = new EmailNotification("alice@example.com",
                "Hello", "Body text");
        Notification sms   = new SmsNotification("+15550100", "Your code: 42");
        Notification push  = new PushNotification("tok-xyz", "Alert", "{\"k\":1}");

        LOG.info(describe(email)); // check → cast → use, three times over
        LOG.info(describe(sms));
        LOG.info(describe(push));

        // Output:
        // INFO: Email to alice@example.com: Hello
        // INFO: SMS to +15550100
        // INFO: Push to tok-xyz: Alert
    }
}