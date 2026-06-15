// Java 9+
module com.domain.order.api {

    // Accessible to all modules that require this one
    exports com.domain.order.api;

    // Accessible ONLY to the billing module.
    // Any other module receives a compile-time error
    // even if it declares requires com.domain.order.api.
    exports com.domain.order.internal.events
            to com.domain.billing;
}
