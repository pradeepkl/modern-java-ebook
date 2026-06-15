// Java 25+
// Feature shown: generic methods and type inference, final in Java 16+

/**
 * Listing 8.4 — GenericMethods.java
 * Demonstrates: generic methods with type parameters inferred at the call site,
 * showing how a single method works across unrelated domain types without casting
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter08;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class GenericMethods {

    private static final Logger log =
            Logger.getLogger(GenericMethods.class.getName());

    // Domain records — two unrelated types
    record OrderRequest(String id, String customerId, double amount, String city) {}
    record LoanApplication(String id, String applicantId, double amount, String purpose) {}
    record ValidationResult(boolean passed, String message) {}

    // Rule interface — parameterised by domain type T
    interface Rule<T> {
        ValidationResult validate(T input);
    }

    // Validator<T> — collects and runs rules for any type T
    static class Validator<T> {
        private final List<Rule<T>> rules;
        Validator(List<Rule<T>> rules) { this.rules = List.copyOf(rules); }
        List<ValidationResult> validate(T input) {
            return rules.stream().map(r -> r.validate(input)).toList();
        }
    }

    // Generic method — T inferred from arguments at the call site
    static <T> Optional<ValidationResult> firstFailure(
            Validator<T> validator, T input) {
        return validator.validate(input).stream()
                .filter(r -> !r.passed())
                .findFirst();
    }

    // Generic method — finds first item matching predicate in any list
    static <T> Optional<T> findFirst(List<T> items, Predicate<T> predicate) {
        return items.stream().filter(predicate).findFirst();
    }

    // Generic method — pairs a value with its validation results
    static <T> Map.Entry<T, List<ValidationResult>> withValidation(
            Validator<T> validator, T input) {
        return Map.entry(input, validator.validate(input));
    }

    void main() {
        // Rule: amount must be positive
        Rule<OrderRequest> positiveAmount =
                o -> new ValidationResult(o.amount() > 0, "Amount must be positive");
        Rule<LoanApplication> positiveLoan =
                l -> new ValidationResult(l.amount() > 0, "Loan amount must be positive");

        Validator<OrderRequest> orderValidator = new Validator<>(List.of(positiveAmount));
        Validator<LoanApplication> loanValidator = new Validator<>(List.of(positiveLoan));

        OrderRequest order = new OrderRequest("ORD-001", "C1", -50.0, "London");
        LoanApplication loan = new LoanApplication("LOAN-001", "A1", -10000.0, "Home");

        // T inferred as OrderRequest — no explicit type argument needed
        firstFailure(orderValidator, order)
                .ifPresent(f -> log.warning("Order failed: " + f.message()));

        // T inferred as LoanApplication — same method, different type
        firstFailure(loanValidator, loan)
                .ifPresent(f -> log.warning("Loan failed: " + f.message()));

        // Generic findFirst — T inferred as String
        findFirst(List.of("alpha", "beta", "gamma"), s -> s.startsWith("b"))
                .ifPresent(s -> log.info("Found: " + s));

        // withValidation — T inferred as OrderRequest
        Map.Entry<OrderRequest, List<ValidationResult>> entry =
                withValidation(orderValidator, order);
        log.info("Validated: " + entry.getKey().id()
                + " results: " + entry.getValue().size());

        // Output:
        // WARNING: Order failed: Amount must be positive
        // WARNING: Loan failed: Loan amount must be positive
        // INFO: Found: beta
        // INFO: Validated: ORD-001 results: 1
    }
}