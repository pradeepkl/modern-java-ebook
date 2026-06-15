// Java 25+
// Feature shown: pattern matching for instanceof (flow sensitivity), final in Java 16+
/**
 * Listing 6.3 — FlowSensitivity.java
 * Demonstrates: Flow sensitivity with instanceof pattern matching and negated patterns.
 * The compiler tracks binding variable scope based on control flow paths.
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 17+ for sealed interfaces; instanceof pattern matching final in Java 16+.
 * Uses void main() instance main method, final in Java 25+ (JEP 512).
 */
package chapter06;

import java.util.logging.Logger;

public class FlowSensitivity {

    private static final Logger log =
            Logger.getLogger(FlowSensitivity.class.getName());

    // Sealed interface restricts permitted subtypes at compile time
    sealed interface Notification
            permits EmailNotification, SmsNotification {}

    record EmailNotification(String to,
            String subject,
            String body) implements Notification {}

    record SmsNotification(String phoneNumber,
            String message) implements Notification {}

    /**
     * Demonstrates negated instanceof pattern — early return avoids nesting.
     * The binding variable e is in scope after the guard because the negation
     * guarantees the pattern matched on the path that continues past the if.
     */
    public static String recipientAddress(Notification notification) {
        // Negated pattern: if NOT an EmailNotification, return early
        if (!(notification instanceof EmailNotification e)) {
            return "non-email recipient";
        }
        // e is in scope here — flow sensitivity guarantees the match succeeded
        return e.to();
    }

    void main() {
        // EmailNotification — pattern matches, binding variable e is used
        Notification email = new EmailNotification(
                "alice@example.com", "Welcome", "Hello Alice");

        // SmsNotification — negated pattern triggers early return
        Notification sms = new SmsNotification(
                "+1-555-0100", "Your code is 4242");

        log.info("Email recipient: " + recipientAddress(email));
        log.info("SMS recipient:   " + recipientAddress(sms));

        // Output:
        // INFO: Email recipient: alice@example.com
        // INFO: SMS recipient:   non-email recipient
    }
}