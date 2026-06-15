// Java 9+
package com.domain.order.impl;

import java.util.List;
import java.util.logging.Logger;

// Package-private — accessible within this package and this module only.
// External modules cannot access it even via reflection
// unless the package is explicitly opened.
class OrderValidator {

    private static final Logger log =
            Logger.getLogger(OrderValidator.class.getName());

    boolean isValid(String customerId, List<String> items) {
        var valid = customerId != null
                && !customerId.isBlank()
                && items != null
                && !items.isEmpty();
        if (!valid) {
            log.warning("Invalid order parameters for customer: "
                    + customerId);
        }
        return valid;
    }
}
