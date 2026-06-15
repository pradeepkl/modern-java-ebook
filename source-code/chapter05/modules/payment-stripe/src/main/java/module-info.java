// Java 9+
module com.domain.payment.stripe {
    requires java.logging;
    requires com.domain.payment.api;

    // StripeProcessor is NOT exported — invisible to consumers.
    // ServiceLoader makes it discoverable without exposing it.
    provides com.domain.payment.api.PaymentProcessor
            with com.domain.payment.stripe.StripeProcessor;
}
