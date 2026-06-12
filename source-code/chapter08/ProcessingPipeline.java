// Java 16+
/**
 * Listing 8.7 — ProcessingPipeline.java
 * Demonstrates: Generic class with bounded type parameter, wildcards (PECS),
 *               generic methods, Stream<T>, and Iterator<T> in a type-safe pipeline
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 16+
 */
package chapter08;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

interface Auditable {
    String referenceId();
    String submittedBy();
    Instant submittedAt();
}

record ValidationResult(boolean passed, String message) {}

interface ValidationRule<T extends Auditable> {
    ValidationResult validate(T item);
}

record OrderRequest(String referenceId, String submittedBy,
                    Instant submittedAt, double amount, String customerId)
        implements Auditable {}

record LoanApplication(String referenceId, String submittedBy,
                       Instant submittedAt, double amount, String applicantId)
        implements Auditable {}

record PositiveAmountRule() implements ValidationRule<OrderRequest> {
    public ValidationResult validate(OrderRequest o) {
        return o.amount() > 0
            ? new ValidationResult(true, "OK")
            : new ValidationResult(false, "Negative amount");
    }
}

record LoanAmountRule() implements ValidationRule<LoanApplication> {
    public ValidationResult validate(LoanApplication l) {
        return l.amount() > 0
            ? new ValidationResult(true, "OK")
            : new ValidationResult(false, "Negative loan");
    }
}

class AuditingValidator<T extends Auditable> {
    private final List<ValidationRule<T>> rules;
    AuditingValidator(List<ValidationRule<T>> rules) { this.rules = rules; }
    List<ValidationResult> validateAndAudit(T item) {
        return rules.stream().map(r -> r.validate(item)).toList();
    }
}

// Generic pipeline — T bounded by Auditable
class Pipeline<T extends Auditable> {

    private static final Logger log =
            Logger.getLogger(Pipeline.class.getName());

    private final AuditingValidator<T> validator;
    private final List<T> processed = new ArrayList<>();
    private final List<T> rejected  = new ArrayList<>();

    Pipeline(AuditingValidator<T> validator) { this.validator = validator; }

    // Producer pattern — List<? extends T> reads from source
    List<T> processBatch(List<? extends T> submissions) {
        submissions.forEach(submission -> {
            boolean valid = validator.validateAndAudit(submission)
                    .stream().allMatch(ValidationResult::passed);
            if (valid) processed.add(submission);
            else       rejected.add(submission);
        });
        log.info("Processed: " + processed.size()
                + "  Rejected: " + rejected.size());
        return List.copyOf(processed);
    }

    // Instance generic method — typed result, no cast needed
    Optional<T> findByReference(String referenceId) {
        return processed.stream()
                .filter(item -> item.referenceId().equals(referenceId))
                .findFirst();
    }

    // Static generic method — query across any auditable list
    static <U extends Auditable> List<U> findBy(
            List<U> items, Predicate<U> predicate) {
        return items.stream().filter(predicate).toList();
    }

    // Typed Iterator — no cast required at call site
    Iterator<T> processedIterator() {
        return List.copyOf(processed).iterator();
    }

    // Consumer pattern — List<? super T> writes to destination
    void transferProcessed(List<? super T> destination) {
        destination.addAll(processed);
    }

    int processedCount() { return processed.size(); }
    int rejectedCount()  { return rejected.size();  }
}

public class ProcessingPipeline {

    private static final Logger log =
            Logger.getLogger(ProcessingPipeline.class.getName());

    public static void main(String[] args) {

        // Two pipelines — same class, two domains; compiler enforces separation
        Pipeline<OrderRequest> orderPipeline =
                new Pipeline<>(new AuditingValidator<>(List.of(new PositiveAmountRule())));

        Pipeline<LoanApplication> loanPipeline =
                new Pipeline<>(new AuditingValidator<>(List.of(new LoanAmountRule())));

        List<OrderRequest> orders = List.of(
                new OrderRequest("ORD-001", "Alice", Instant.now(),  99.99, "C1"),
                new OrderRequest("ORD-002", "Bob",   Instant.now(), -50.0,  "C2"),
                new OrderRequest("ORD-003", "Carol", Instant.now(), 149.99, "C3"));

        List<LoanApplication> loans = List.of(
                new LoanApplication("LOAN-001", "Dave", Instant.now(),  50000.0, "A1"),
                new LoanApplication("LOAN-002", "Eve",  Instant.now(), -1000.0,  "A2"));

        List<OrderRequest>    validOrders = orderPipeline.processBatch(orders);
        List<LoanApplication> validLoans  = loanPipeline.processBatch(loans);

        // findByReference — typed result, no cast
        orderPipeline.findByReference("ORD-001")
                .ifPresent(o -> log.info("Found: " + o.referenceId()
                        + " by " + o.submittedBy()));

        // Static generic method with predicate
        List<OrderRequest> highValue =
                Pipeline.findBy(validOrders, o -> o.amount() > 100.0);
        log.info("High-value orders: " + highValue.size());

        // Consumer pattern — transfer to combined List<Auditable>
        List<Auditable> allProcessed = new ArrayList<>();
        orderPipeline.transferProcessed(allProcessed);
        loanPipeline.transferProcessed(allProcessed);
        log.info("Total processed across domains: " + allProcessed.size());

        // Typed Iterator — no cast at call site
        Iterator<OrderRequest> orderIterator = orderPipeline.processedIterator();
        while (orderIterator.hasNext()) {
            OrderRequest o = orderIterator.next(); // no cast required
            log.info("Iterated: " + o.referenceId());
        }

        log.info("Orders — processed: " + orderPipeline.processedCount()
                + "  rejected: " + orderPipeline.rejectedCount());
        log.info("Loans  — processed: " + loanPipeline.processedCount()
                + "  rejected: " + loanPipeline.rejectedCount());

        // Output:
        // INFO: Processed: 2  Rejected: 1
        // INFO: Processed: 1  Rejected: 1
        // INFO: Found: ORD-001 by Alice
        // INFO: High-value orders: 1
        // INFO: Total processed across domains: 3
        // INFO: Iterated: ORD-001
        // INFO: Iterated: ORD-003
        // INFO: Orders — processed: 2  rejected: 1
        // INFO: Loans  — processed: 1  rejected: 1
    }
}