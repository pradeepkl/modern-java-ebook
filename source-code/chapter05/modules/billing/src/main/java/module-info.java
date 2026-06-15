// Java 9+


module com.domain.billing {
    requires java.logging;
    // BillingService can use OrderService and Order.
    // BillingService cannot use OrderServiceImpl.
    // BillingService cannot use OrderRepository.
    // The compiler enforces all three statements.
    requires com.domain.order.api;
}
