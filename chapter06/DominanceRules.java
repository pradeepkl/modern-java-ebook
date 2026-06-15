// Java 25+
// Feature shown: pattern matching for switch (dominance rules), final in Java 21+

/**
 * Listing 6.9 — DominanceRules.java
 * Demonstrates: Dominance rules in pattern matching switch expressions.
 * Specific type patterns must appear before general ones; a broader
 * pattern that subsumes a narrower one causes a compile error.
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 21+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter06;

import java.util.logging.Logger;

public class DominanceRules {

    private static final Logger LOG =
            Logger.getLogger(DominanceRules.class.getName());

    // Sealed interface: compiler knows every permitted subtype
    sealed interface Notification
            permits EmailNotification, SmsNotification {}

    // Permitted subtype 1
    record EmailNotification(String to,
            String subject,
            String body) implements Notification {}

    // Permitted subtype 2
    record SmsNotification(String phoneNumber,
            String message) implements Notification {}

    /**
     * Routes a notification to the correct handler.
     * Specific arms must appear before any general type pattern.
     * Placing "case Notification n" first would dominate all arms
     * below it and cause a compile error.
     */
    public static String route(Notification notification) {
        return switch (notification) {
            // Correct: specific subtypes listed before any general pattern
            case EmailNotification e ->
                    "smtp-router: " + e.to();       // matched first
            case SmsNotification s ->
                    "sms-gateway: " + s.phoneNumber(); // matched second
            // Adding "case Notification n -> ..." here would be legal
            // (general after specific), but adding it BEFORE the above
            // arms would be a compile error — it would dominate them.
        };
    }

    void main() {
        Notification email = new EmailNotification(
                "alice@example.com", "Hello", "Body text");
        Notification sms = new SmsNotification(
                "+15550001234", "Your code is 9876");

        // Dispatch each notification through the pattern switch
        LOG.info(route(email)); // specific EmailNotification arm matches
        LOG.info(route(sms));   // specific SmsNotification arm matches

        // Output:
        // INFO: smtp-router: alice@example.com
        // INFO: sms-gateway: +15550001234
    }
}