// Java 9+


module com.domain.order {

    // Only the api package is accessible to other modules.
    // The impl package is strongly encapsulated — invisible
    // externally even though its classes may be public.
    exports com.domain.order.api;

    // This module depends on the money module.
    // Without this declaration, references to Money types
    // are a compile error — even if the JAR is on the module path.
    requires com.domain.money;
}
