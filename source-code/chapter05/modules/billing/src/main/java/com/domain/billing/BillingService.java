// Java 9+
package com.domain.billing;

import com.domain.order.api.Order;
import com.domain.order.api.OrderService;
// import com.domain.order.impl.OrderServiceImpl; ← compile error
// OrderServiceImpl is not exported. The module boundary is enforced.

import java.math.BigDecimal;
import java.util.logging.Logger;

public class BillingService {

    private static final Logger log =
            Logger.getLogger(BillingService.class.getName());

    private final OrderService orderService;

    public BillingService(OrderService orderService) {
        this.orderService = orderService;
    }

    public BigDecimal calculateBill(String orderId) {
        return orderService.findOrder(orderId)
                .map(this::computeAmount)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Order not found: " + orderId));
    }

    private BigDecimal computeAmount(Order order) {
        var amount = BigDecimal.valueOf(
                order.items().size() * 9.99);
        log.info("Computed bill for order "
                + order.orderId() + ": " + amount);
        return amount;
    }
}
