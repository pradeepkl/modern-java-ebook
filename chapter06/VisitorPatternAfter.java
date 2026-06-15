// Java 25+
// Feature shown: pattern matching for switch, final in Java 21+

/**
 * Listing 6.15 — VisitorPatternAfter.java
 * Demonstrates: Switch pattern matching replacing the Visitor pattern
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 21+ for pattern matching for switch; Java 25+ for
 * the void main() instance main method (JEP 512)
 */
package chapter06;

import java.util.logging.Logger;

public class VisitorPatternAfter {

    private static final Logger log =
            Logger.getLogger(VisitorPatternAfter.class.getName());

    // Sealed hierarchy — compiler knows all permitted subtypes
    sealed interface Notification
            permits EmailNotification, SmsNotification {}

    record EmailNotification(String to,
                             String subject,
                             String body) implements Notification {}

    record SmsNotification(String phoneNumber,
                           String message) implements Notification {}

    // One method. No visitor infrastructure.
    // Compiler-verified exhaustiveness.
    public static String describe(Notification notification) {
        return switch (notification) {
            case EmailNotification e ->
                    "Email to " + e.to();           // matched and bound
            case SmsNotification s ->
                    "SMS to " + s.phoneNumber();    // matched and bound
        };
    }

    void main() {
        Notification email = new EmailNotification(
                "alice@example.com", "Hello", "Hi there");

        Notification sms = new SmsNotification(
                "+1-555-0100", "Your code is 4242");

        // No visitor object, no accept() call, no boilerplate
        log.info(describe(email));
        log.info(describe(sms));

        // Output:
        // INFO: Email to alice@example.com
        // INFO: SMS to +1-555-0100
    }
}