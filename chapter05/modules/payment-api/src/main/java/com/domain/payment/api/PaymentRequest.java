// Java 16+
package com.domain.payment.api;

public record PaymentRequest(String orderId, double amount) {}
