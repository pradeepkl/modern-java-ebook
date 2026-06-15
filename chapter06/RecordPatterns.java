// Java 25+
// Feature shown: record patterns (deconstruction in switch), final in Java 21+

/**
 * Listing 6.11 — RecordPatterns.java
 * Demonstrates: record pattern deconstruction inside switch expressions,
 *               binding record components directly at the match site
 * Chapter 6: Pattern Matching in Modern Java
 * Requires: Java 21+ for record patterns; void main() final in Java 25+ (JEP 512)
 */
package chapter06;

import java.util.logging.Logger;

public class RecordPatterns {

    private static final Logger log =
            Logger.getLogger(RecordPatterns.class.getName());

    // Sealed interface — permits only the two record types below
    sealed interface ApiResult permits Success, Failure {}

    // Record components are deconstructed directly in the switch arm
    record Success(String data, int statusCode) implements ApiResult {}

    record Failure(String error, int statusCode, String requestId)
            implements ApiResult {}

    // No accessor calls needed — components are bound by the pattern
    static String summarise(ApiResult result) {
        return switch (result) {
            // Deconstruct Success: bind data and statusCode inline
            case Success(var data, var code) ->
                    "OK " + code + ": " + data;
            // Deconstruct Failure: bind error, statusCode, and requestId inline
            case Failure(var error, var code, var reqId) ->
                    "ERR " + code + ": " + error + " (req=" + reqId + ")";
        };
    }

    void main() {
        ApiResult ok  = new Success("User created", 201);
        ApiResult err = new Failure("Not found", 404, "req-abc-123");

        // Each result is deconstructed at the match site — no getters needed
        log.info(summarise(ok));
        log.info(summarise(err));

        // Nested: a Success with a low status code treated as informational
        ApiResult info = new Success("Accepted", 202);
        log.info(summarise(info));

        // Output:
        // INFO: OK 201: User created
        // INFO: ERR 404: Not found (req=req-abc-123)
        // INFO: OK 202: Accepted
    }
}