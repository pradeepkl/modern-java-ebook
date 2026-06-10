// Java 17+
/**
 * Listing 6.3 — FlowSensitivity.java
 * Demonstrates: Flow-sensitive instanceof pattern matching with negated patterns
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 17+
 */
package chapter06;

import java.util.logging.Logger;

public class FlowSensitivity {

    private static final Logger log = Logger.getLogger(
            FlowSensitivity.class.getName());

    // Sealed interface restricts permitted subtypes to two records
    sealed interface Notification
            permits EmailNotification, SmsNotification {}

    record EmailNotification(String to,
            String subject,
            String body) implements Notification {}

    record SmsNotification(String phoneNumber,
            String message) implements Notification {}

    public static String recipientAddress(
            Notification notification) {
        // Negated pattern: if NOT an EmailNotification, return early
        if (!(notification instanceof EmailNotification e)) {
            return "non-email recipient";
        }
        // Flow sensitivity: compiler knows 'e' is in scope here
        // because the negated guard guarantees the pattern matched
        return e.to();
    }

    public static void main(String[] args) {
        // EmailNotification — pattern matches, binding variable e is used
        Notification email = new EmailNotification(
                "alice@example.com", "Welcome", "Hello Alice");

        // SmsNotification — negated guard triggers early return
        Notification sms = new SmsNotification(
                "+1-555-0100", "Your code is 4242");

        log.info("Email recipient : " + recipientAddress(email));
        log.info("SMS recipient   : " + recipientAddress(sms));

        // Output:
        // INFO: Email recipient : alice@example.com
        // INFO: SMS recipient   : non-email recipient
    }
}