// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 4.2 — ExceptionVsOptional.java
 * Demonstrates: Contrasting exception-based and Optional-based API design
 * for signalling absence of a value versus unexpected failure.
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter04;

import java.util.Optional;
import java.util.logging.Logger;

public class ExceptionVsOptional {

    private static final Logger LOG = Logger.getLogger(ExceptionVsOptional.class.getName());

    // Simple User record to represent a domain object
    record User(Long id, String name) {}

    // Checked exception signals that absence is unexpected and must be handled
    static class UserNotFoundException extends Exception {
        UserNotFoundException(Long userId) {
            super("No user found with id: " + userId);
        }
    }

    // Exception-based: absence is an error — caller MUST handle the failure
    static User loadUserById(Long userId) throws UserNotFoundException {
        if (userId == null || userId <= 0) {
            throw new UserNotFoundException(userId); // signals unexpected absence
        }
        return new User(userId, "Alice");
    }

    // Optional-based: absence is a normal outcome — caller decides what to do
    static Optional<User> findUserById(Long userId) {
        if (userId == null || userId <= 0) {
            return Optional.empty(); // signals valid, expected absence
        }
        return Optional.of(new User(userId, "Alice"));
    }

    void main() {
        // Demonstrate exception-based lookup — absence is treated as a failure
        try {
            User user = loadUserById(42L);
            LOG.info("loadUserById found: " + user.name()); // expected success
            loadUserById(-1L); // triggers UserNotFoundException
        } catch (UserNotFoundException e) {
            LOG.warning("Exception-based lookup failed: " + e.getMessage());
        }

        // Demonstrate Optional-based lookup — absence is a normal, handled outcome
        Optional<User> found = findUserById(42L);
        found.ifPresent(u -> LOG.info("findUserById found: " + u.name()));

        Optional<User> missing = findUserById(-1L);
        String result = missing.map(User::name).orElse("user not present");
        LOG.info("findUserById result for invalid id: " + result);

        // Output:
        // INFO: loadUserById found: Alice
        // WARNING: Exception-based lookup failed: No user found with id: -1
        // INFO: findUserById found: Alice
        // INFO: findUserById result for invalid id: user not present
    }
}