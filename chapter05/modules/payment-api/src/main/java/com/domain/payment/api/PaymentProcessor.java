// Java 9+
package com.domain.payment.api;

public interface PaymentProcessor {
    PaymentResult process(PaymentRequest request);
}
