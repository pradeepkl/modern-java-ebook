// Java 8+
/**
 * Listing 7.7 — OptionalContract.java
 * Demonstrates: Using Optional to make absence explicit in API contracts
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 8+
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
            return stored != null && stored.email().equals(email) ? stored : null;
        }

        @Override
        public Optional<Customer> findByEmail(String email) {
            // Returns empty Optional when not found — absence is explicit
            if (stored != null && stored.email().equals(email)) {
                return Optional.of(stored);
            }
            return Optional.empty();
        }
    }

    public static void notify(
            CustomerRepository repo,
            String email,
            Customer defaultCustomer) {

        // ifPresent — act only when value exists
        repo.findByEmail(email)
                .ifPresent(c -> log.info("Notifying: " + c.name()));

        // orElse — provide a safe fallback when absent
        Customer result = repo.findByEmail(email)
                .orElse(defaultCustomer);

        // map — transform if present, propagate absence
        Optional<String> name = repo.findByEmail(email)
                .map(Customer::name);

        log.info("Result: " + result.name());
        log.info("Name optional: " + name);
    }

    public static void main(String[] args) {
        Customer alice = new Customer("1", "Alice", "alice@example.com");
        Customer fallback = new Customer("0", "Unknown", "none");
        CustomerRepository repo = new InMemoryRepo(alice);

        log.info("--- Known email ---");
        notify(repo, "alice@example.com", fallback); // Alice found

        log.info("--- Unknown email ---");
        notify(repo, "ghost@example.com", fallback); // fallback used

        // Output:
        // --- Known email ---
        // Notifying: Alice
        // Result: Alice
        // Name optional: Optional[Alice]
        // --- Unknown email ---
        // Result: Unknown
        // Name optional: Optional.empty
    }
}