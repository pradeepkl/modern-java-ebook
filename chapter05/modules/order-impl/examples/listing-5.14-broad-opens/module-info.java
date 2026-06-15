// Java 9+
module com.domain.order.impl {
    requires com.domain.order.api;

    // Broad opens — every module gains reflective access.
    // Defeats strong encapsulation. Acceptable during migration only.
    opens com.domain.order.impl;

    // Always prefer a targeted form:
    // opens com.domain.order.impl to spring.core;
}
