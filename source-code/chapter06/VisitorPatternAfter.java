// Java 21+
/**
 * Listing 6.15 — VisitorPatternAfter.java
 * Demonstrates: Switch pattern matching replacing the Visitor pattern
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 21+
 */
package chapter06;

import java.util.logging.Logger;

public class VisitorPatternAfter {

    private static final Logger log = Logger.getLogger(VisitorPatternAfter.class.getName());

    // Sealed interface — compiler knows all permitted subtypes
    sealed interface Notification
            permits EmailNotification, SmsNotification {}

    // Records are transparent value carriers — no accept() needed
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
                    "Email to " + e.to();           // pattern binds e directly
            case SmsNotification s ->
                    "SMS to " + s.phoneNumber();    // pattern binds s directly
        };
    }

    public static void main(String[] args) {

        Notification email = new EmailNotification(
                "alice@example.com", "Hello", "Hi there");

        Notification sms = new SmsNotification(
                "+1-555-0100", "Your code is 4242");

        // No visitor object required — just call describe()
        log.info(describe(email));
        log.info(describe(sms));

        // Output:
        // INFO: Email to alice@example.com
        // INFO: SMS to +1-555-0100
    }
}