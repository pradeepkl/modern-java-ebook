// Java 9+
package com.domain.order.api;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createOrder(String customerId, List<String> items);
    Optional<Order> findOrder(String orderId);
    void cancelOrder(String orderId);
}
