// Java 16+
/**
 * Listing 8.3 — GenericValidator.java
 * Demonstrates: Generic Validator<T> class with type-safe rule composition
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 16+
 */
package chapter08;

import java.util.List;
import java.util.logging.Logger;

// Domain records — two separate domains
record OrderRequest(String customerId, double amount) {}
record LoanApplication(String applicantId, double requestedAmount) {}

// Immutable result record
record ValidationResult(String ruleName, boolean passed, String message) {
    static ValidationResult pass(String ruleName) {
        return new ValidationResult(ruleName, true, "");
    }
    static ValidationResult fail(String ruleName, String message) {
        return new ValidationResult(ruleName, false, message);
    }
}

// Rule<T> — single validation rule for any type T
interface Rule<T> {
    ValidationResult validate(T input);
}

// Rules for OrderRequest
class PositiveAmountRule implements Rule<OrderRequest> {
    public ValidationResult validate(OrderRequest input) {
        return input.amount() > 0
            ? ValidationResult.pass("positive-amount")
            : ValidationResult.fail("positive-amount", "Amount must be positive");
    }
}

class CustomerIdRule implements Rule<OrderRequest> {
    public ValidationResult validate(OrderRequest input) {
        return input.customerId() != null && !input.customerId().isBlank()
            ? ValidationResult.pass("customer-id")
            : ValidationResult.fail("customer-id", "Customer ID required");
    }
}

// Rules for LoanApplication
class LoanAmountRule implements Rule<LoanApplication> {
    public ValidationResult validate(LoanApplication input) {
        return input.requestedAmount() > 0
            ? ValidationResult.pass("loan-amount")
            : ValidationResult.fail("loan-amount", "Requested amount must be positive");
    }
}

class ApplicantIdRule implements Rule<LoanApplication> {
    public ValidationResult validate(LoanApplication input) {
        return input.applicantId() != null && !input.applicantId().isBlank()
            ? ValidationResult.pass("applicant-id")
            : ValidationResult.fail("applicant-id", "Applicant ID required");
    }
}

// Validator<T> — collects Rule<T> instances and runs them against T
class Validator<T> {
    private static final Logger log = Logger.getLogger(Validator.class.getName());
    private final List<Rule<T>> rules; // rules are parameterised by T

    Validator(List<Rule<T>> rules) {
        this.rules = List.copyOf(rules); // defensive copy
    }

    // validate(T input) — T is consistent throughout
    List<ValidationResult> validate(T input) {
        return rules.stream()
                .map(rule -> rule.validate(input)) // each rule receives T
                .toList();
    }

    boolean isValid(T input) {
        return validate(input).stream().allMatch(ValidationResult::passed);
    }
}

public class GenericValidator {
    private static final Logger log = Logger.getLogger(GenericValidator.class.getName());

    public static void main(String[] args) {
        // Validator<OrderRequest> — holds only Rule<OrderRequest>
        Validator<OrderRequest> orderValidator = new Validator<>(List.of(
                new PositiveAmountRule(),
                new CustomerIdRule()));

        // Validator<LoanApplication> — holds only Rule<LoanApplication>
        Validator<LoanApplication> loanValidator = new Validator<>(List.of(
                new LoanAmountRule(),
                new ApplicantIdRule()));

        OrderRequest validOrder = new OrderRequest("C-001", 250.0);
        OrderRequest badOrder  = new OrderRequest("", -10.0);

        LoanApplication validLoan = new LoanApplication("A-99", 5000.0);
        LoanApplication badLoan   = new LoanApplication(null, -1.0);

        log.info("=== Order Validation ===");
        orderValidator.validate(validOrder)
                .forEach(r -> log.info(r.ruleName() + " passed=" + r.passed()));
        log.info("validOrder isValid: " + orderValidator.isValid(validOrder));

        orderValidator.validate(badOrder)
                .forEach(r -> log.info(r.ruleName() + " passed=" + r.passed() +
                        (r.passed() ? "" : " | " + r.message())));
        log.info("badOrder isValid: " + orderValidator.isValid(badOrder));

        log.info("=== Loan Validation ===");
        loanValidator.validate(validLoan)
                .forEach(r -> log.info(r.ruleName() + " passed=" + r.passed()));
        log.info("validLoan isValid: " + loanValidator.isValid(validLoan));

        loanValidator.validate(badLoan)
                .forEach(r -> log.info(r.ruleName() + " passed=" + r.passed() +
                        (r.passed() ? "" : " | " + r.message())));
        log.info("badLoan isValid: " + loanValidator.isValid(badLoan));

        // Output:
        // positive-amount passed=true
        // customer-id passed=true
        // validOrder isValid: true
        // positive-amount passed=false | Amount must be positive
        // customer-id passed=false | Customer ID required
        // badOrder isValid: false
        // loan-amount passed=true
        // applicant-id passed=true
        // validLoan isValid: true
        // loan-amount passed=false | Requested amount must be positive
        // applicant-id passed=false | Applicant ID required
        // badLoan isValid: false
    }
}