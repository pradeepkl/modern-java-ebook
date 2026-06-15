// Java 9+
module com.domain.order.impl {
    requires java.logging;
    requires com.domain.order.api;
    requires com.domain.payment.api;

    // uses declares: this module needs a PaymentProcessor.
    // The module system ensures one is available at runtime
    // without the consumer knowing which implementation it is.
    uses com.domain.payment.api.PaymentProcessor;
}
