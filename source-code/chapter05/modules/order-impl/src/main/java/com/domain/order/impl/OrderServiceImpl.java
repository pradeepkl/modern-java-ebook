// Java 9+
package com.domain.order.impl;

import com.domain.order.api.Order;
import com.domain.order.api.OrderService;
import com.domain.order.api.OrderStatus;
import com.domain.payment.api.PaymentProcessor;
import com.domain.payment.api.PaymentRequest;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.logging.Logger;

public class OrderServiceImpl implements OrderService {

    private static final Logger log =
            Logger.getLogger(OrderServiceImpl.class.getName());

    // Discover the PaymentProcessor at runtime.
    // The consumer never references StripeProcessor directly.
    // Swapping implementations requires only a module path change.
    private final PaymentProcessor paymentProcessor =
            ServiceLoader.load(PaymentProcessor.class)
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalStateException(
                                "No PaymentProcessor found"));

    @Override
    public Order createOrder(
            String customerId, List<String> items) {

        var order = new Order(
                UUID.randomUUID().toString(),
                customerId,
                items,
                OrderStatus.PENDING);

        log.info("Creating order: " + order.orderId()
                + " for customer: " + customerId);

        var request = new PaymentRequest(
                order.orderId(),
                items.size() * 9.99);

        var result = paymentProcessor.process(request);

        if (result.success()) {
            log.info("Payment successful for order: "
                    + order.orderId());
            return new Order(
                    order.orderId(),
                    order.customerId(),
                    order.items(),
                    OrderStatus.PROCESSING);
        }

        log.warning("Payment failed for order: "
                + order.orderId());
        throw new IllegalStateException(
                "Payment failed for order: "
                + order.orderId());
    }

    @Override
    public Optional<Order> findOrder(String orderId) {
        // OrderRepository (package-private data access
        // abstraction) would be used here in a full
        // implementation — hidden from all consumers.
        return Optional.empty();
    }

    @Override
    public void cancelOrder(String orderId) {
        log.info("Cancelling order: " + orderId);
        // Delegates to OrderRepository internally.
    }
}
