// Java 25+
// Feature shown: wildcards and PECS (Producer Extends, Consumer Super), final in Java 5+

/**
 * Listing 8.6 — WildcardsAndPECS.java
 * Demonstrates: wildcards and PECS (Producer Extends, Consumer Super)
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter08;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WildcardsAndPECS {

    private static final Logger log =
            Logger.getLogger(WildcardsAndPECS.class.getName());

    // Auditable interface — the shared contract for both domains
    interface Auditable {
        String referenceId();
        String submittedBy();
        Instant submittedAt();
    }

    // OrderRequest record — implements Auditable
    record OrderRequest(String referenceId, String submittedBy,
                        Instant submittedAt, double amount, String customerId)
            implements Auditable {}

    // LoanApplication record — implements Auditable
    record LoanApplication(String referenceId, String submittedBy,
                           Instant submittedAt, double loanAmount, String applicantId)
            implements Auditable {}

    // PRODUCER pattern — reads from the list, never writes
    // List<? extends Auditable> accepts List<OrderRequest> or List<LoanApplication>
    static void printSummary(List<? extends Auditable> submissions) {
        submissions.forEach(s ->
                log.info(s.referenceId() + " by " + s.submittedBy()));
    }

    // CONSUMER pattern — writes to the list, never reads domain fields
    // Accepts List<OrderRequest>, List<Auditable>, or List<Object>
    static void collect(OrderRequest request, List<? super OrderRequest> destination) {
        destination.add(request); // safe: destination accepts OrderRequest or wider
    }

    // PECS in one method — source produces, destination consumes
    static <T extends Auditable> void transfer(
            List<? extends T> source,      // producer: read from it
            List<? super T> destination) { // consumer: write to it
        source.forEach(destination::add);
    }

    void main() {
        List<OrderRequest> orders = List.of(
                new OrderRequest("ORD-001", "Alice", Instant.now(), 99.99, "C1"),
                new OrderRequest("ORD-002", "Bob", Instant.now(), 149.99, "C2"));

        List<LoanApplication> loans = List.of(
                new LoanApplication("LOAN-001", "Carol", Instant.now(), 50000.0, "A1"));

        // Same method accepts both list types — wildcard in action
        printSummary(orders); // List<OrderRequest>
        printSummary(loans);  // List<LoanApplication>

        // PECS transfer — copy orders and loans into a combined Auditable list
        List<Auditable> combined = new ArrayList<>();
        transfer(orders, combined); // T inferred as OrderRequest
        transfer(loans, combined);  // T inferred as LoanApplication
        log.info("Combined size: " + combined.size());

        // Output:
        // INFO: ORD-001 by Alice
        // INFO: ORD-002 by Bob
        // INFO: LOAN-001 by Carol
        // INFO: Combined size: 3
    }
}