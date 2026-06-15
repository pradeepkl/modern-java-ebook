module com.domain.order.api {
    // Money appears in exported Order record.
    // Consumers get Money access automatically.
    requires transitive com.domain.money;
    exports com.domain.order.api;
}
