// Java 9+
module com.domain.order.impl {
    requires com.domain.order.api;

    // Spring needs reflective access to read @Transactional
    // annotations and inject dependencies into fields.
    // opens grants exactly that permission to spring.core only.
    opens com.domain.order.impl to spring.core;

    // Multiple frameworks if needed:
    // opens com.domain.order.impl to spring.core, hibernate.core;
}
