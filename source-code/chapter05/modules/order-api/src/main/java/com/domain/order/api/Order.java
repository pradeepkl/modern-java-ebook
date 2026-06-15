// Java 16+
package com.domain.order.api;

import java.util.List;

public record Order(
        String orderId,
        String customerId,
        List<String> items,
        OrderStatus status) {
}
