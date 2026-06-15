// Java 25+
// Feature shown: sealed classes and instance main methods, final in Java 17+

/**
 * Listing 6.1 — TraditionalTypeDispatch.java
 * Demonstrates: Traditional type dispatch using instanceof checks and explicit casts
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter06;

import java.util.logging.Logger;

public class TraditionalTypeDispatch {

    private static final Logger LOG =
            Logger.getLogger(TraditionalTypeDispatch.class.getName());

    // A sealed notification hierarchy — three known subtypes
    sealed interface Notification
            permits EmailNotification, SmsNotification, PushNotification {}

    record EmailNotification(String to, String subject, String body)
            implements Notification {}

    record SmsNotification(String phoneNumber, String message)
            implements Notification {}

    record PushNotification(String deviceToken, String title, String payload)
            implements Notification {}

    // Traditional dispatch: check, cast, use — three steps every time
    static String describe(Notification notification) {
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
        throw new IllegalArgumentException(
                "Unknown notification: " + notification);
    }

    void main() {
        Notification email = new EmailNotification(
                "alice@example.com", "Hello", "Body text");
        Notification sms   = new SmsNotification("+15550001234", "Your code is 42");
        Notification push  = new PushNotification(
                "token-xyz", "Alert", "{\"msg\":\"hi\"}");

        LOG.info(describe(email)); // Email to alice@example.com: Hello
        LOG.info(describe(sms));   // SMS to +15550001234
        LOG.info(describe(push));  // Push to token-xyz: Alert

        // Output:
        // INFO: Email to alice@example.com: Hello
        // INFO: SMS to +15550001234
        // INFO: Push to token-xyz: Alert
    }
}