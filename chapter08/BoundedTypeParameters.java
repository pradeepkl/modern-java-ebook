// Java 25+
// Feature shown: bounded type parameters with generics, final in Java 21+

/**
 * Listing 8.5 — BoundedTypeParameters.java
 * Demonstrates: bounded type parameters (T extends Auditable) allowing
 * the compiler to guarantee method availability on T at compile time
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter08;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

interface Auditable {
    String referenceId();
    String submittedBy();
    Instant submittedAt();
}

record OrderRequest(
        String referenceId, String submittedBy,
        Instant submittedAt, double amount, String customerId)
        implements Auditable {}

record LoanApplication(
        String referenceId, String submittedBy,
        Instant submittedAt, double requestedAmount, String applicantId)
        implements Auditable {}

record AuditEntry(String referenceId, String submittedBy,
        Instant validatedAt, boolean passed) {}

record ValidationResult(String ruleName, boolean passed, String message) {}

interface Rule<T> { ValidationResult validate(T input); }

class PositiveAmountRule implements Rule<OrderRequest> {
    public ValidationResult validate(OrderRequest o) {
        boolean ok = o.amount() > 0;
        return new ValidationResult("PositiveAmount", ok,
                ok ? "OK" : "Amount must be positive");
    }
}

class LoanAmountRule implements Rule<LoanApplication> {
    public ValidationResult validate(LoanApplication l) {
        boolean ok = l.requestedAmount() > 0;
        return new ValidationResult("LoanAmount", ok,
                ok ? "OK" : "Loan amount must be positive");
    }
}

class AuditingValidator<T extends Auditable> {
    private static final Logger log =
            Logger.getLogger(AuditingValidator.class.getName());
    private final List<Rule<T>> rules;
    private final List<AuditEntry> auditLog = new ArrayList<>();

    AuditingValidator(List<Rule<T>> rules) { this.rules = List.copyOf(rules); }

    List<ValidationResult> validateAndAudit(T input) {
        List<ValidationResult> results = rules.stream()
                .map(rule -> rule.validate(input)).toList();
        boolean allPassed = results.stream().allMatch(ValidationResult::passed);
        // Bound guarantees referenceId() and submittedBy() exist on T
        auditLog.add(new AuditEntry(input.referenceId(),
                input.submittedBy(), Instant.now(), allPassed));
        log.info("Validated [" + input.referenceId()
                + "] by [" + input.submittedBy()
                + "] passed: " + allPassed);
        return results;
    }

    List<AuditEntry> auditLog() { return List.copyOf(auditLog); }
}

public class BoundedTypeParameters {
    void main() {
        // T = OrderRequest — bound satisfied, referenceId() accessible
        AuditingValidator<OrderRequest> orderValidator =
                new AuditingValidator<>(List.of(new PositiveAmountRule()));
        orderValidator.validateAndAudit(new OrderRequest(
                "ORD-001", "alice", Instant.now(), -50.0, "C1"));

        // T = LoanApplication — same validator, different domain type
        AuditingValidator<LoanApplication> loanValidator =
                new AuditingValidator<>(List.of(new LoanAmountRule()));
        loanValidator.validateAndAudit(new LoanApplication(
                "LOAN-001", "bob", Instant.now(), 15000.0, "A1"));

        // AuditingValidator<String> would NOT compile — String is not Auditable
        Logger.getLogger(BoundedTypeParameters.class.getName())
                .info("Audit entries: " + orderValidator.auditLog().size());
        // Output:
        // INFO: Validated [ORD-001] by [alice] passed: false
        // INFO: Validated [LOAN-001] by [bob] passed: true
        // INFO: Audit entries: 1
    }
}