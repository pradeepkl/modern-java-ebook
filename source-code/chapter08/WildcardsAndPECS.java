// Java 16+
/**
 * Listing 8.6 — WildcardsAndPECS.java
 * Demonstrates: Wildcards and the PECS principle (Producer Extends, Consumer Super)
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 16+
 */
package chapter08;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

// Auditable interface — the shared contract
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

public class WildcardsAndPECS {

    private static final Logger log =
            Logger.getLogger(WildcardsAndPECS.class.getName());

    // PRODUCER pattern — reads from list, never writes
    // List<? extends Auditable> accepts List<OrderRequest> or List<LoanApplication>
    static void printSummary(List<? extends Auditable> submissions) {
        submissions.forEach(s ->
                log.info(s.referenceId() + " by " + s.submittedBy()));
    }

    // CONSUMER pattern — writes to list, never reads domain fields
    // Accepts List<OrderRequest>, List<Auditable>, or List<Object>
    static void collect(OrderRequest request, List<? super OrderRequest> destination) {
        destination.add(request); // safe to add OrderRequest into a supertype list
    }

    // PECS in one method — source PRODUCES (extends), destination CONSUMES (super)
    static <T extends Auditable> void transfer(
            List<? extends T> source,
            List<? super T> destination) {
        source.forEach(destination::add); // read from source, write to destination
    }

    public static void main(String[] args) {
        List<OrderRequest> orders = List.of(
                new OrderRequest("ORD-001", "Alice", Instant.now(), 99.99, "C1"),
                new OrderRequest("ORD-002", "Bob", Instant.now(), 149.99, "C2"));

        List<LoanApplication> loans = List.of(
                new LoanApplication("LOAN-001", "Carol", Instant.now(), 50000.0, "A1"));

        // Same method handles both list types — wildcard makes this possible
        printSummary(orders);  // List<OrderRequest>
        printSummary(loans);   // List<LoanApplication>

        // PECS in action — transfer both into a combined List<Auditable>
        List<Auditable> combined = new ArrayList<>();
        transfer(orders, combined); // source=List<OrderRequest>, dest=List<Auditable>
        transfer(loans, combined);  // source=List<LoanApplication>, dest=List<Auditable>
        log.info("Combined size: " + combined.size()); // 3

        // Output:
        // INFO: ORD-001 by Alice
        // INFO: ORD-002 by Bob
        // INFO: LOAN-001 by Carol
        // INFO: ORD-001 by Alice
        // INFO: ORD-002 by Bob
        // INFO: LOAN-001 by Carol
        // INFO: Combined size: 3
    }
}