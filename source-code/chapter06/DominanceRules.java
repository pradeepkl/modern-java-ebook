// Java 21+
/**
 * Listing 6.9 — DominanceRules.java
 * Demonstrates: Pattern dominance rules in sealed switch expressions
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 21+
 */
package chapter06;

import java.util.logging.Logger;

public class DominanceRules {

    private static final Logger LOG = Logger.getLogger(DominanceRules.class.getName());

    // Sealed interface — compiler knows all permitted subtypes
    sealed interface Notification
            permits EmailNotification, SmsNotification {}

    record EmailNotification(String to,
            String subject,
            String body) implements Notification {}

    record SmsNotification(String phoneNumber,
            String message) implements Notification {}

    /**
     * Routes a notification to the correct handler.
     * Specific arms must appear before general ones to avoid dominance errors.
     */
    public static String route(Notification notification) {
        return switch (notification) {
            // ✅ Most specific arms listed first
            // ❌ If "case Notification n" appeared here, it would dominate
            //    all subtypes below it — compiler would reject the file.
            case EmailNotification e ->
                    "smtp-router: " + e.to();       // matched by exact type
            case SmsNotification s ->
                    "sms-gateway: " + s.phoneNumber(); // matched by exact type
        };
    }

    public static void main(String[] args) {
        // Create sample notifications
        Notification email = new EmailNotification(
                "alice@example.com", "Hello", "Body text");
        Notification sms = new SmsNotification(
                "+441234567890", "Your code is 9876");

        // Route each notification — specific pattern arms fire correctly
        LOG.info(route(email)); // smtp-router arm
        LOG.info(route(sms));   // sms-gateway arm

        // Output:
        // smtp-router: alice@example.com
        // sms-gateway: +441234567890
    }
}