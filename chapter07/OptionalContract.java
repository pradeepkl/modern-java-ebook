// Java 25+
// Feature shown: Optional as an explicit absence contract, final in Java 8+

/**
 * Listing 7.7 — OptionalContract.java
 * Demonstrates: Using Optional to make absence an explicit part of the API contract
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter07;

import java.util.Optional;
import java.util.logging.Logger;

public class OptionalContract {

    private static final Logger log =
            Logger.getLogger(OptionalContract.class.getName());

    record Customer(String id, String name, String email) {}

    interface CustomerRepository {
        // Caller cannot tell whether null is expected
        Customer findByEmailUnsafe(String email);

        // Absence is part of the API contract
        Optional<Customer> findByEmail(String email);
    }

    static class InMemoryRepo implements CustomerRepository {
        private final Customer stored;

        InMemoryRepo(Customer stored) { this.stored = stored; }

        @Override
        public Customer findByEmailUnsafe(String email) {
            return stored.email().equals(email) ? stored : null;
        }

        @Override
        public Optional<Customer> findByEmail(String email) {
            return stored.email().equals(email)
                    ? Optional.of(stored)
                    : Optional.empty();
        }
    }

    static void notify(CustomerRepository repo,
                       String email,
                       Customer defaultCustomer) {

        // ifPresent — act only when value exists
        repo.findByEmail(email)
                .ifPresent(c -> log.info("Notifying: " + c.name()));

        // orElse — provide a safe fallback
        Customer result = repo.findByEmail(email)
                .orElse(defaultCustomer);

        // map — transform if present, propagate absence
        Optional<String> name = repo.findByEmail(email)
                .map(Customer::name);

        log.info("Result: " + result.name());
        log.info("Name optional: " + name);
    }

    void main() {
        Customer alice = new Customer("1", "Alice", "alice@example.com");
        Customer fallback = new Customer("0", "Unknown", "none@example.com");
        CustomerRepository repo = new InMemoryRepo(alice);

        notify(repo, "alice@example.com", fallback);  // found
        notify(repo, "missing@example.com", fallback); // absent — fallback used

        // Output:
        // INFO: Notifying: Alice
        // INFO: Result: Alice
        // INFO: Name optional: Optional[Alice]
        // INFO: Result: Unknown
        // INFO: Name optional: Optional.empty
    }
}