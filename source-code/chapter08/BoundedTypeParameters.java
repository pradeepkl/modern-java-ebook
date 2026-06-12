// Java 16+
/**
 * Listing 8.5 — BoundedTypeParameters.java
 * Demonstrates: Bounded type parameters with T extends Auditable
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 16+
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

interface Rule<T> {
    ValidationResult validate(T input);
}

record ValidationResult(boolean passed, String message) {}

record OrderRequest(
        String referenceId, String submittedBy,
        Instant submittedAt, double amount, String customerId)
        implements Auditable {}

record LoanApplication(
        String referenceId, String submittedBy,
        Instant submittedAt, double requestedAmount, String applicantId)
        implements Auditable {}

record AuditEntry(
        String referenceId, String submittedBy,
        Instant validatedAt, boolean passed) {}

class PositiveAmountRule implements Rule<OrderRequest> {
    public ValidationResult validate(OrderRequest o) {
        return o.amount() > 0
                ? new ValidationResult(true, "OK")
                : new ValidationResult(false, "Amount must be positive");
    }
}

class LoanAmountRule implements Rule<LoanApplication> {
    public ValidationResult validate(LoanApplication l) {
        return l.requestedAmount() > 0
                ? new ValidationResult(true, "OK")
                : new ValidationResult(false, "Loan amount must be positive");
    }
}

class AuditingValidator<T extends Auditable> {
    private static final Logger log = Logger.getLogger(AuditingValidator.class.getName());
    private final List<Rule<T>> rules;
    private final List<AuditEntry> auditLog = new ArrayList<>();

    AuditingValidator(List<Rule<T>> rules) { this.rules = List.copyOf(rules); }

    List<ValidationResult> validateAndAudit(T input) {
        List<ValidationResult> results = rules.stream().map(r -> r.validate(input)).toList();
        boolean allPassed = results.stream().allMatch(ValidationResult::passed);
        auditLog.add(new AuditEntry(input.referenceId(), input.submittedBy(), Instant.now(), allPassed));
        log.info("Validated [" + input.referenceId() + "] by [" + input.submittedBy() + "] passed=" + allPassed);
        return results;
    }

    List<AuditEntry> auditLog() { return List.copyOf(auditLog); }
}

public class BoundedTypeParameters {
    private static final Logger log = Logger.getLogger(BoundedTypeParameters.class.getName());

    public static void main(String[] args) {
        // T=OrderRequest — bound satisfied, referenceId()/submittedBy() accessible
        AuditingValidator<OrderRequest> orderValidator =
                new AuditingValidator<>(List.of(new PositiveAmountRule()));
        orderValidator.validateAndAudit(
                new OrderRequest("ORD-001", "alice", Instant.now(), 250.0, "C1"));
        orderValidator.validateAndAudit(
                new OrderRequest("ORD-002", "bob", Instant.now(), -10.0, "C2"));

        // T=LoanApplication — same generic class, different bound-satisfying type
        AuditingValidator<LoanApplication> loanValidator =
                new AuditingValidator<>(List.of(new LoanAmountRule()));
        loanValidator.validateAndAudit(
                new LoanApplication("LOAN-001", "carol", Instant.now(), 5000.0, "A1"));

        log.info("Order audit entries: " + orderValidator.auditLog().size());
        log.info("Loan audit entries : " + loanValidator.auditLog().size());
        // Output:
        // INFO: Validated [ORD-001] by [alice] passed=true
        // INFO: Validated [ORD-002] by [bob]   passed=false
        // INFO: Validated [LOAN-001] by [carol] passed=true
        // INFO: Order audit entries: 2
        // INFO: Loan audit entries : 1
    }
}