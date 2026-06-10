// Java 21+
/**
 * Listing 6.11 — RecordPatterns.java
 * Demonstrates: Record pattern deconstruction in switch expressions
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 21+
 */
package chapter06;

import java.util.logging.Logger;

public class RecordPatterns {

    private static final Logger log = Logger.getLogger(
            RecordPatterns.class.getName());

    // Sealed interface restricts permitted subtypes to Success and Failure
    sealed interface ApiResult
            permits Success, Failure {}

    // Record components are deconstructed directly in the switch arms
    record Success(String data, int statusCode) implements ApiResult {}

    record Failure(String error, int statusCode, String requestId)
            implements ApiResult {}

    /**
     * Deconstructs ApiResult components at the match site —
     * no accessor calls required after the match.
     */
    public static String summarise(ApiResult result) {
        return switch (result) {
            // Bind data and code directly from the Success record
            case Success(var data, var code) ->
                    "OK " + code + ": " + data;
            // Bind error, code, and reqId directly from the Failure record
            case Failure(var error, var code, var reqId) ->
                    "ERR " + code + ": " + error + " (req=" + reqId + ")";
        };
    }

    public static void main(String[] args) {

        // Construct a successful API result
        ApiResult ok = new Success("User created", 201);

        // Construct a failed API result with a request ID for tracing
        ApiResult err = new Failure("Not found", 404, "req-abc-123");

        // Summarise each result using record pattern deconstruction
        log.info(summarise(ok));
        log.info(summarise(err));

        // Output:
        // INFO: OK 201: User created
        // INFO: ERR 404: Not found (req=req-abc-123)
    }
}