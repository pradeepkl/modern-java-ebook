// Java 25+
// Feature shown: bounded generic type parameters and wildcards (PECS), final in Java 21+

/**
 * Listing 8.7 — ProcessingPipeline.java
 * Demonstrates: class-level bounded type parameter, generic methods,
 *               wildcards (PECS), Stream, and Iterator in one pipeline
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter08;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

sealed interface Auditable permits OrderRequest, LoanApplication {
    String referenceId();
    String submittedBy();
    Instant submittedAt();
    double amount();
}

record OrderRequest(String referenceId, String submittedBy,
                    Instant submittedAt, double amount,
                    String customerId) implements Auditable {}

record LoanApplication(String referenceId, String submittedBy,
                       Instant submittedAt, double amount,
                       String accountId) implements Auditable {}

record ValidationResult(boolean passed, String message) {}

@FunctionalInterface
interface ValidationRule<T extends Auditable> {
    ValidationResult validate(T item);
}

class AuditingValidator<T extends Auditable> {
    private final List<ValidationRule<T>> rules;
    AuditingValidator(List<ValidationRule<T>> rules) { this.rules = rules; }
    List<ValidationResult> validateAndAudit(T item) {
        return rules.stream().map(r -> r.validate(item)).toList();
    }
}

class Pipeline<T extends Auditable> {
    private static final Logger log =
            Logger.getLogger(Pipeline.class.getName());
    private final AuditingValidator<T> validator;
    private final List<T> processed = new ArrayList<>();
    private final List<T> rejected  = new ArrayList<>();

    Pipeline(AuditingValidator<T> validator) { this.validator = validator; }

    // Producer — List<? extends T> (PECS: extends for reading)
    List<T> processBatch(List<? extends T> submissions) {
        submissions.forEach(s -> {
            boolean valid = validator.validateAndAudit(s)
                    .stream().allMatch(ValidationResult::passed);
            if (valid) processed.add(s); else rejected.add(s);
        });
        log.info("Processed: " + processed.size() + " Rejected: " + rejected.size());
        return List.copyOf(processed);
    }

    Optional<T> findByReference(String referenceId) {
        return processed.stream()
                .filter(i -> i.referenceId().equals(referenceId)).findFirst();
    }

    // Generic static method — works across any Auditable pipeline
    static <T extends Auditable> List<T> findBy(List<T> items, Predicate<T> p) {
        return items.stream().filter(p).toList();
    }

    Iterator<T> processedIterator() { return List.copyOf(processed).iterator(); }

    // Consumer — List<? super T> (PECS: super for writing)
    void transferProcessed(List<? super T> destination) { destination.addAll(processed); }

    int processedCount() { return processed.size(); }
    int rejectedCount()  { return rejected.size();  }
}

class ProcessingPipeline {
    private static final Logger log =
            Logger.getLogger(ProcessingPipeline.class.getName());

    void main() {
        ValidationRule<OrderRequest>    positiveAmount =
                o -> new ValidationResult(o.amount() > 0, "amount must be positive");
        ValidationRule<LoanApplication> loanAmount     =
                l -> new ValidationResult(l.amount() > 0, "loan amount must be positive");

        Pipeline<OrderRequest>    orderPipeline =
                new Pipeline<>(new AuditingValidator<>(List.of(positiveAmount)));
        Pipeline<LoanApplication> loanPipeline  =
                new Pipeline<>(new AuditingValidator<>(List.of(loanAmount)));

        List<OrderRequest> orders = List.of(
                new OrderRequest("ORD-001", "Alice", Instant.now(), 99.99,  "C1"),
                new OrderRequest("ORD-002", "Bob",   Instant.now(), -50.0,  "C2"),
                new OrderRequest("ORD-003", "Carol", Instant.now(), 149.99, "C3"));

        List<LoanApplication> loans = List.of(
                new LoanApplication("LOAN-001", "Dave", Instant.now(),  50000.0, "A1"),
                new LoanApplication("LOAN-002", "Eve",  Instant.now(), -1000.0,  "A2"));

        List<OrderRequest>    validOrders = orderPipeline.processBatch(orders);
        List<LoanApplication> validLoans  = loanPipeline.processBatch(loans);

        Optional<OrderRequest> found = orderPipeline.findByReference("ORD-001");
        found.ifPresent(o -> log.info("Found: " + o.referenceId()));

        List<OrderRequest> highValue =
                Pipeline.findBy(validOrders, o -> o.amount() > 100.0);
        log.info("High-value orders: " + highValue.size());

        List<Auditable> allProcessed = new ArrayList<>();
        orderPipeline.transferProcessed(allProcessed);
        loanPipeline.transferProcessed(allProcessed);
        log.info("Total processed across domains: " + allProcessed.size());

        Iterator<OrderRequest> it = orderPipeline.processedIterator();
        while (it.hasNext()) {
            OrderRequest o = it.next(); // no cast needed — typed Iterator
            log.info("Order: " + o.referenceId() + " amount=" + o.amount());
        }
        // Output:
        // INFO Processed: 2 Rejected: 1
        // INFO Processed: 1 Rejected: 1
        // INFO Found: ORD-001
        // INFO High-value orders: 1
        // INFO Total processed across domains: 3
        // INFO Order: ORD-001 amount=99.99
        // INFO Order: ORD-003 amount=149.99
    }
}