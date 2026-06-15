// Java 25+
// Feature shown: records and generic interfaces, final in Java 16+

/**
 * Listing 8.2 — GenericRuleInterface.java
 * Demonstrates: Generic interface Rule<T> as a type-safe validation contract,
 *               with concrete implementations for different domain types.
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter08;

import java.util.logging.Logger;

// Domain records — T will be bound to one of these at use time
record OrderRequest(String customerId, double amount, String deliveryAddress) {}
record LoanApplication(String referenceId, String applicantId,
        double requestedAmount, String purpose) {}

// ValidationResult — immutable, carries the outcome
record ValidationResult(String ruleName, boolean passed, String message) {

    static ValidationResult pass(String ruleName) {
        return new ValidationResult(ruleName, true, "");
    }

    static ValidationResult fail(String ruleName, String message) {
        return new ValidationResult(ruleName, false, message);
    }
}

// Rule<T> — a single validation rule for any type T
// T is the placeholder — unknown at definition time, known at use time
interface Rule<T> {
    ValidationResult validate(T input);
}

// Concrete rule — T is now OrderRequest
// The compiler knows exactly what validate() receives
class PositiveAmountRule implements Rule<OrderRequest> {
    @Override
    public ValidationResult validate(OrderRequest input) {
        return input.amount() > 0
                ? ValidationResult.pass("positive-amount")
                : ValidationResult.fail("positive-amount",
                        "Amount must be positive");
    }
}

// Same interface — T is now LoanApplication
class LoanAmountRule implements Rule<LoanApplication> {
    @Override
    public ValidationResult validate(LoanApplication input) {
        return input.requestedAmount() > 0
                ? ValidationResult.pass("loan-amount")
                : ValidationResult.fail("loan-amount",
                        "Requested amount must be positive");
    }
}

public class GenericRuleInterface {

    private static final Logger log =
            Logger.getLogger(GenericRuleInterface.class.getName());

    void main() {
        // Each rule is bound to its specific domain type at compile time
        Rule<OrderRequest> orderRule = new PositiveAmountRule();
        Rule<LoanApplication> loanRule = new LoanAmountRule();

        OrderRequest validOrder =
                new OrderRequest("C001", 150.0, "123 Main St");
        OrderRequest invalidOrder =
                new OrderRequest("C002", -10.0, "456 Elm St");
        LoanApplication validLoan =
                new LoanApplication("L001", "A001", 5000.0, "Home");
        LoanApplication invalidLoan =
                new LoanApplication("L002", "A002", -1.0, "Car");

        log.info(orderRule.validate(validOrder).toString());
        log.info(orderRule.validate(invalidOrder).toString());
        log.info(loanRule.validate(validLoan).toString());
        log.info(loanRule.validate(invalidLoan).toString());

        // Output:
        // ValidationResult[ruleName=positive-amount, passed=true, message=]
        // ValidationResult[ruleName=positive-amount, passed=false, message=Amount must be positive]
        // ValidationResult[ruleName=loan-amount, passed=true, message=]
        // ValidationResult[ruleName=loan-amount, passed=false, message=Requested amount must be positive]
    }
}