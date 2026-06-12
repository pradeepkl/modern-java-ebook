// Java 16+
/**
 * Listing 8.2 — GenericRuleInterface.java
 * Demonstrates: Generic interface as a type-safe validation contract
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 16+
 */
package chapter08;

import java.util.logging.Logger;

// Domain records — two unrelated types, same validation pattern
record OrderRequest(String orderId, String customerId,
                    double amount, String deliveryAddress) {}

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
class PositiveAmountRule implements Rule<OrderRequest> {
    @Override
    public ValidationResult validate(OrderRequest input) {
        return input.amount() > 0
                ? ValidationResult.pass("positive-amount")
                : ValidationResult.fail("positive-amount", "Amount must be positive");
    }
}

// Same interface — T is now LoanApplication
class LoanAmountRule implements Rule<LoanApplication> {
    @Override
    public ValidationResult validate(LoanApplication input) {
        return input.requestedAmount() > 0
                ? ValidationResult.pass("loan-amount")
                : ValidationResult.fail("loan-amount", "Requested amount must be positive");
    }
}

public class GenericRuleInterface {
    private static final Logger log = Logger.getLogger(GenericRuleInterface.class.getName());

    public static void main(String[] args) {
        // Rule<OrderRequest> — compiler enforces the type at validate()
        Rule<OrderRequest> orderRule = new PositiveAmountRule();
        OrderRequest validOrder = new OrderRequest("O1", "C1", 150.0, "123 Main St");
        OrderRequest badOrder   = new OrderRequest("O2", "C2", -10.0, "456 Elm St");

        ValidationResult r1 = orderRule.validate(validOrder);
        ValidationResult r2 = orderRule.validate(badOrder);

        // Rule<LoanApplication> — same interface, different type parameter
        Rule<LoanApplication> loanRule = new LoanAmountRule();
        LoanApplication validLoan = new LoanApplication("L1", "A1", 5000.0, "Home");
        LoanApplication badLoan   = new LoanApplication("L2", "A2", 0.0,    "Car");

        ValidationResult r3 = loanRule.validate(validLoan);
        ValidationResult r4 = loanRule.validate(badLoan);

        log.info("Order valid:   " + r1);
        log.info("Order invalid: " + r2);
        log.info("Loan valid:    " + r3);
        log.info("Loan invalid:  " + r4);
        // Output:
        // Order valid:   ValidationResult[ruleName=positive-amount, passed=true,  message=]
        // Order invalid: ValidationResult[ruleName=positive-amount, passed=false, message=Amount must be positive]
        // Loan valid:    ValidationResult[ruleName=loan-amount,     passed=true,  message=]
        // Loan invalid:  ValidationResult[ruleName=loan-amount,     passed=false, message=Requested amount must be positive]
    }
}