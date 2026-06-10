// Java 8+
/**
 * Listing 6.14 — VisitorPatternBefore.java
 * Demonstrates: Classic Visitor pattern used before pattern matching
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 8+
 */
package chapter06;

import java.util.logging.Logger;

public class VisitorPatternBefore {

    private static final Logger log = Logger.getLogger(
            VisitorPatternBefore.class.getName());

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

    // Visitor interface — adding a new type requires changing this contract
    interface NotificationVisitor<T> {
        T visitEmail(EmailNotification e);
        T visitSms(SmsNotification s);
    }

    // Every new operation requires a new Visitor implementation class
    static class DescribeVisitor implements NotificationVisitor<String> {
        @Override
        public String visitEmail(EmailNotification e) {
            return "Email to " + e.to() + " | Subject: " + e.subject();
        }

        @Override
        public String visitSms(SmsNotification s) {
            return "SMS to " + s.phoneNumber() + " | Message: " + s.message();
        }
    }

    public static void main(String[] args) {
        DescribeVisitor visitor = new DescribeVisitor();

        Notification email = new EmailNotification(
                "alice@example.com", "Hi", "Hello there");
        Notification sms = new SmsNotification("+1-555-0100", "Your code is 42");

        // Double dispatch: notification calls back into the visitor
        String emailDesc = email.accept(visitor);
        String smsDesc = sms.accept(visitor);

        log.info(emailDesc);
        log.info(smsDesc);

        // Output:
        // INFO: Email to alice@example.com | Subject: Hi
        // INFO: SMS to +1-555-0100 | Message: Your code is 42
    }
}