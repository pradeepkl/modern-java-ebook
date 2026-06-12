// Java 16+
/**
 * Listing 8.4 — GenericMethods.java
 * Demonstrates: Generic methods with type inference across multiple domain types
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 16+
 */
package chapter08;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

// Supporting records and types defined in the same file
record ValidationResult(boolean passed, String message) {}
record OrderRequest(String id, String customerId, double amount, String city) {}
record LoanApplication(String id, String applicantId, double amount, String purpose) {}

interface Rule<T> { ValidationResult validate(T input); }

class Validator<T> {
    private final List<Rule<T>> rules;
    Validator(List<Rule<T>> rules) { this.rules = List.copyOf(rules); }
    List<ValidationResult> validate(T input) {
        return rules.stream().map(r -> r.validate(input)).toList();
    }
}

class PositiveAmountRule implements Rule<OrderRequest> {
    public ValidationResult validate(OrderRequest o) {
        return o.amount() > 0
            ? new ValidationResult(true, "OK")
            : new ValidationResult(false, "Order amount must be positive: " + o.amount());
    }
}

class LoanAmountRule implements Rule<LoanApplication> {
    public ValidationResult validate(LoanApplication l) {
        return l.amount() > 0
            ? new ValidationResult(true, "OK")
            : new ValidationResult(false, "Loan amount must be positive: " + l.amount());
    }
}

public class GenericMethods {

    private static final Logger log = Logger.getLogger(GenericMethods.class.getName());

    // Generic method — <T> declared before the return type; T inferred at call site
    static <T> Optional<ValidationResult> firstFailure(Validator<T> validator, T input) {
        return validator.validate(input).stream().filter(r -> !r.passed()).findFirst();
    }

    // Generic method — finds first item matching predicate; works for any type
    static <T> Optional<T> findFirst(List<T> items, Predicate<T> predicate) {
        return items.stream().filter(predicate).findFirst();
    }

    // Generic method — wraps value with its validation results; return type preserves T
    static <T> Map.Entry<T, List<ValidationResult>> withValidation(Validator<T> v, T input) {
        return Map.entry(input, v.validate(input));
    }

    public static void main(String[] args) {
        Validator<OrderRequest> orderValidator = new Validator<>(List.of(new PositiveAmountRule()));
        OrderRequest order = new OrderRequest("ORD-001", "C1", -50.0, "London");

        // T = OrderRequest inferred from arguments — no explicit type witness needed
        Optional<ValidationResult> failure = firstFailure(orderValidator, order);
        failure.ifPresent(f -> log.warning("Failed: " + f.message()));

        // Same generic method — different type, identical behaviour
        Validator<LoanApplication> loanValidator = new Validator<>(List.of(new LoanAmountRule()));
        LoanApplication loan = new LoanApplication("LOAN-001", "A1", -10000.0, "Home");

        Optional<ValidationResult> loanFailure = firstFailure(loanValidator, loan);
        loanFailure.ifPresent(f -> log.warning("Loan failed: " + f.message()));

        // findFirst — generic utility, works for any list and predicate
        List<String> cities = List.of("London", "Paris", "Berlin");
        Optional<String> found = findFirst(cities, c -> c.startsWith("P"));
        found.ifPresent(c -> log.info("Found city: " + c));

        // withValidation — entry preserves the exact type T
        Map.Entry<OrderRequest, List<ValidationResult>> entry = withValidation(orderValidator, order);
        log.info("Validated: " + entry.getKey().id() + " results=" + entry.getValue().size());

        // Output:
        // WARNING: Failed: Order amount must be positive: -50.0
        // WARNING: Loan failed: Loan amount must be positive: -10000.0
        // INFO: Found city: Paris
        // INFO: Validated: ORD-001 results=1
    }
}