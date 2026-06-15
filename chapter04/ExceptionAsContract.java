// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

package chapter04;

import java.util.logging.Logger;

/**
 * Listing 4.1 — ExceptionAsContract.java
 * Demonstrates: exceptions as part of a public API contract, using a checked
 * exception to make failure modes explicit and intentional in method signatures
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
public class ExceptionAsContract {

    private static final Logger LOG = Logger.getLogger(ExceptionAsContract.class.getName());

    // Domain record representing a user
    record User(Long id, String name) {}

    // Checked exception signals a named, intentional failure mode
    static class UserNotFoundException extends Exception {
        private final Long userId;

        UserNotFoundException(Long userId) {
            super("No user found with id: " + userId);
            this.userId = userId;
        }

        Long getUserId() {
            return userId;
        }
    }

    // Repository interface — failure is part of the public contract
    interface UserRepository {
        // The throws clause is a formal API guarantee: absence is exceptional
        User loadUserById(Long userId) throws UserNotFoundException;
    }

    // Stub implementation: only user id 1 exists
    static class InMemoryUserRepository implements UserRepository {
        @Override
        public User loadUserById(Long userId) throws UserNotFoundException {
            if (userId == 1L) {
                return new User(1L, "Alice");
            }
            throw new UserNotFoundException(userId); // explicit, named failure
        }
    }

    void main() {
        UserRepository repo = new InMemoryUserRepository();

        for (long id : new long[]{1L, 42L}) {
            try {
                User user = repo.loadUserById(id); // caller must handle failure
                LOG.info("Loaded user: " + user.name());
            } catch (UserNotFoundException ex) {
                LOG.warning("User not found for id: " + ex.getUserId());
            }
        }

        // Output:
        // INFO: Loaded user: Alice
        // WARNING: User not found for id: 42
    }
}