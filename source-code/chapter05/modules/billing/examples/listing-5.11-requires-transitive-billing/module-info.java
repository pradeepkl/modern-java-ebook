module com.domain.billing {
    requires com.domain.order.api;
    // Money is transitively available — no explicit requires needed.
}
