// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+
package chapter06;

import java.util.logging.Logger;

/**
 * Listing 6.14 — VisitorPatternBefore.java
 * Demonstrates: Visitor pattern used before pattern matching to avoid
 * instanceof chains — every type must implement accept(), and every
 * new operation requires a new Visitor class.
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
public class VisitorPatternBefore {

    private static final Logger log =
            Logger.getLogger(VisitorPatternBefore.class.getName());

    // Open interface — every type must implement accept()
    interface Notification {
        <T> T accept(NotificationVisitor<T> visitor);
    }

    record EmailNotification(String to, String subject, String body)
            implements Notification {
        @Override
        public <T> T accept(NotificationVisitor<T> visitor) {
            return visitor.visitEmail(this); // dispatch to visitor
        }
    }

    record SmsNotification(String phoneNumber, String message)
            implements Notification {
        @Override
        public <T> T accept(NotificationVisitor<T> visitor) {
            return visitor.visitSms(this); // dispatch to visitor
        }
    }

    // Visitor interface — one method per concrete type
    interface NotificationVisitor<T> {
        T visitEmail(EmailNotification e);
        T visitSms(SmsNotification s);
    }

    // Every new operation requires a new Visitor implementation
    static class DescribeVisitor implements NotificationVisitor<String> {
        @Override
        public String visitEmail(EmailNotification e) {
            return "Email to " + e.to();
        }
        @Override
        public String visitSms(SmsNotification s) {
            return "SMS to " + s.phoneNumber();
        }
    }

    void main() {
        Notification email = new EmailNotification(
                "alice@example.com", "Hi", "Hello");
        Notification sms = new SmsNotification("+1234567890", "Hello");

        DescribeVisitor visitor = new DescribeVisitor();

        String emailDesc = email.accept(visitor); // double dispatch
        String smsDesc   = sms.accept(visitor);

        log.info(emailDesc);
        log.info(smsDesc);
        // Output:
        // INFO: Email to alice@example.com
        // INFO: SMS to +1234567890
    }
}