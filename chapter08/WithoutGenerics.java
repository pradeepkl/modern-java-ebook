// Java 25+
// Feature shown: records, final in Java 16+

/**
 * Listing 8.1 — WithoutGenerics.java
 * Demonstrates: Duplicated validator classes before generics are applied.
 * Each domain requires its own concrete validator with identical structure.
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter08;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

// Domain records — identical pipeline shape, different field names
record OrderRequest(String referenceId, String customerId,
                    double amount, String deliveryAddress) {}

record LoanApplication(String referenceId, String applicantId,
                       double requestedAmount, String purpose) {}

// Validator for OrderRequest — no sharing possible without generics
class OrderValidator {
    List<String> validate(OrderRequest order) {
        List<String> violations = new ArrayList<>();
        if (order.amount() <= 0)
            violations.add("Amount must be positive");
        if (order.customerId() == null || order.customerId().isBlank())
            violations.add("Customer ID required");
        return violations;
    }
}

// Validator for LoanApplication — structurally identical, but a separate class
class LoanValidator {
    List<String> validate(LoanApplication loan) {
        List<String> violations = new ArrayList<>();
        if (loan.requestedAmount() <= 0)
            violations.add("Amount must be positive");
        if (loan.applicantId() == null || loan.applicantId().isBlank())
            violations.add("Applicant ID required");
        return violations;
    }
}

public class WithoutGenerics {

    private static final Logger LOG =
            Logger.getLogger(WithoutGenerics.class.getName());

    void main() {
        // Each domain requires its own validator — no reuse, no abstraction
        OrderValidator orderValidator = new OrderValidator();
        LoanValidator loanValidator   = new LoanValidator();

        OrderRequest orderRequest =
                new OrderRequest("ORD-001", "CUST-42", 199.99, "123 Main St");
        LoanApplication loanApplication =
                new LoanApplication("LOAN-007", "", -500.0, "Home renovation");

        List<String> orderViolations = orderValidator.validate(orderRequest);
        List<String> loanViolations  = loanValidator.validate(loanApplication);

        LOG.info("Order violations: " + orderViolations);
        LOG.info("Loan violations:  " + loanViolations);
        // Output:
        // Order violations: []
        // Loan violations:  [Amount must be positive, Applicant ID required]
    }
}