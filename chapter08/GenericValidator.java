// Java 25+
// Feature shown: records and generics for type-safe APIs, final in Java 16+

/**
 * Listing 8.3 — GenericValidator.java
 * Demonstrates: Validator&lt;T&gt; — a generic class whose type parameter
 * flows through constructor, fields, and methods, enforcing type safety
 * across multiple domains without casting.
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter08;

import java.util.List;
import java.util.logging.Logger;

// Domain records — two separate domains, one Validator class
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

// Concrete rules for OrderRequest
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

// Concrete rules for LoanApplication
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

// Validator<T> — type parameter T flows through the entire class
class Validator<T> {

    private static final Logger log =
            Logger.getLogger(Validator.class.getName());

    // List<Rule<T>> — rules are parameterised by T
    private final List<Rule<T>> rules;

    Validator(List<Rule<T>> rules) {
        this.rules = List.copyOf(rules);
    }

    // validate(T input) — T is consistent throughout
    List<ValidationResult> validate(T input) {
        return rules.stream()
                .map(rule -> rule.validate(input))
                .toList();
    }

    boolean isValid(T input) {
        return validate(input).stream()
                .allMatch(ValidationResult::passed);
    }
}

public class GenericValidator {

    private static final Logger log =
            Logger.getLogger(GenericValidator.class.getName());

    void main() {
        // Validator<OrderRequest> — holds only Rule<OrderRequest>
        Validator<OrderRequest> orderValidator =
                new Validator<>(List.of(
                        new PositiveAmountRule(),
                        new CustomerIdRule()));

        // Validator<LoanApplication> — holds only Rule<LoanApplication>
        Validator<LoanApplication> loanValidator =
                new Validator<>(List.of(
                        new LoanAmountRule(),
                        new ApplicantIdRule()));

        OrderRequest validOrder = new OrderRequest("C-001", 250.0);
        OrderRequest badOrder  = new OrderRequest("", -10.0);

        LoanApplication validLoan = new LoanApplication("A-99", 5000.0);
        LoanApplication badLoan   = new LoanApplication(null, -1.0);

        // Each validator accepts only its own domain type
        log.info("Order valid: " + orderValidator.isValid(validOrder));
        log.info("Bad order valid: " + orderValidator.isValid(badOrder));

        orderValidator.validate(badOrder)
                .forEach(r -> log.info("Order rule [" + r.ruleName()
                        + "] passed=" + r.passed()
                        + (r.passed() ? "" : " msg=" + r.message())));

        log.info("Loan valid: " + loanValidator.isValid(validLoan));
        log.info("Bad loan valid: " + loanValidator.isValid(badLoan));

        loanValidator.validate(badLoan)
                .forEach(r -> log.info("Loan rule [" + r.ruleName()
                        + "] passed=" + r.passed()
                        + (r.passed() ? "" : " msg=" + r.message())));

        // The lines below do NOT compile — wrong type for the validator:
        // orderValidator.validate(validLoan);
        // loanValidator.validate(validOrder);

        // Output:
        // Order valid: true
        // Bad order valid: false
        // Order rule [positive-amount] passed=false msg=Amount must be positive
        // Order rule [customer-id] passed=false msg=Customer ID required
        // Loan valid: true
        // Bad loan valid: false
        // Loan rule [loan-amount] passed=false msg=Requested amount must be positive
        // Loan rule [applicant-id] passed=false msg=Applicant ID required
    }
}