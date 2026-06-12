// Java 16+
/**
 * Listing 16.3 — BuilderPattern.java
 * Demonstrates: Builder pattern reimagined with records and fluent builders
 * Chapter 16: Design Patterns Reimagined with Modern Java
 * Requires: Java 16+
 */
package chapter16;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

public class BuilderPattern {

    private static final Logger log =
            Logger.getLogger(MethodHandles.lookup()
                    .lookupClass().getName());

    // Fixed: the record structure (fields, types)
    // Variable: the values provided at construction
    record OrderRequest(String customerId,
            double amount, String region) {

        // Compact constructor — validation at construction time
        OrderRequest {
            if (amount <= 0)
                throw new IllegalArgumentException(
                        "Amount must be positive");
            if (customerId == null || customerId.isBlank())
                throw new IllegalArgumentException(
                        "Customer ID required");
            region = region != null
                    ? region.toUpperCase() : "UNKNOWN";
        }
    }

    record ReportConfig(String format, String region,
            boolean includeVat, int maxRows,
            String sortField) {}

    static class ReportConfigBuilder {
        private String format      = "PDF";
        private String region      = "ALL";
        private boolean includeVat = true;
        private int maxRows        = 100;
        private String sortField   = "amount";

        ReportConfigBuilder format(String f)     { this.format = f;      return this; }
        ReportConfigBuilder region(String r)     { this.region = r;      return this; }
        ReportConfigBuilder maxRows(int n)       { this.maxRows = n;     return this; }
        ReportConfigBuilder includeVat(boolean v){ this.includeVat = v;  return this; }

        ReportConfig build() {
            return new ReportConfig(format, region,
                    includeVat, maxRows, sortField);
        }
    }

    public static void main(String[] args) {

        BuilderPattern bp = new BuilderPattern();

        // Record — direct construction, validated immediately
        var request = new OrderRequest("C1", 99.99, "uk");
        log.info(request.customerId()
                + " £" + request.amount()
                + " " + request.region()); // region normalised to uppercase

        // Compact constructor rejects invalid state at creation
        try {
            new OrderRequest("C1", -50.0, "UK");
        } catch (IllegalArgumentException e) {
            log.info("Rejected: " + e.getMessage());
        }

        // Builder — optional fields configured fluently
        var config = new ReportConfigBuilder()
                .format("CSV")
                .region("UK")
                .maxRows(500)
                .build();
        log.info(config.format()
                + " " + config.region()
                + " rows=" + config.maxRows()
                + " vat=" + config.includeVat());

        // Output:
        // C1 £99.99 UK
        // Rejected: Amount must be positive
        // CSV UK rows=500 vat=true
    }
}