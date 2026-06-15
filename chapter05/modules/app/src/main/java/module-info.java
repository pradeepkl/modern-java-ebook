// Java 9+
module com.domain.app {
    requires java.base;           // always included
    requires java.net.http;       // HTTP client
    requires java.logging;        // logging
    requires com.domain.order.api;
    requires com.domain.payment.api;

    // java.desktop, java.sql, java.xml.bind → not required
    // → not included in the runtime image
}
