// Java 16+
/**
 * Listing 8.1 — WithoutGenerics.java
 * Demonstrates: Duplicated validator classes before generics are applied
 * Chapter 8: Designing Type-Safe APIs with Generics
 * Requires: Java 16+
 */
package chapter08;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

// Domain records — identical structure, different field names
record OrderRequest(String referenceId, String customerId,
                    double amount, String deliveryAddress) {}

record LoanApplication(String referenceId, String applicantId,
                       double requestedAmount, String purpose) {}

// NOT IDEAL: Two validators — identical structure, duplicated logic
// Every change must be applied twice.
// Every bug fix must be applied twice.
// Every new domain requires a third copy.
class OrderValidator {
    List<String> validate(OrderRequest order) {
        List<String> violations = new ArrayList<>();
        if (order.amount() <= 0)
            violations.add("Amount must be positive");       // same rule
        if (order.customerId() == null || order.customerId().isBlank())
            violations.add("Customer ID required");          // same rule
        return violations;
    }
}

class LoanValidator {
    List<String> validate(LoanApplication loan) {
        List<String> violations = new ArrayList<>();
        if (loan.requestedAmount() <= 0)
            violations.add("Amount must be positive");       // duplicated rule
        if (loan.applicantId() == null || loan.applicantId().isBlank())
            violations.add("Applicant ID required");         // duplicated rule
        return violations;
    }
}

public class WithoutGenerics {

    private static final Logger log =
            Logger.getLogger(WithoutGenerics.class.getName());

    public static void main(String[] args) {

        // Each domain requires its own validator — no sharing, no reuse
        OrderValidator orderValidator = new OrderValidator();
        LoanValidator  loanValidator  = new LoanValidator();

        OrderRequest   orderRequest   =
                new OrderRequest("ORD-1", "", -50.0, "123 Main St");
        LoanApplication loanApplication =
                new LoanApplication("LOAN-1", null, 0.0, "Home renovation");

        List<String> orderViolations = orderValidator.validate(orderRequest);
        List<String> loanViolations  = loanValidator.validate(loanApplication);

        log.info("Order violations:  " + orderViolations);
        log.info("Loan violations:   " + loanViolations);

        // Output:
        // Order violations:  [Amount must be positive, Customer ID required]
        // Loan violations:   [Amount must be positive, Applicant ID required]
    }
}