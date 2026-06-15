// Java 16+
package com.domain.payment.api;

public record PaymentResult(boolean success, String orderId) {

    public static PaymentResult success(String orderId) {
        return new PaymentResult(true, orderId);
    }

    public static PaymentResult failure(String orderId) {
        return new PaymentResult(false, orderId);
    }
}
