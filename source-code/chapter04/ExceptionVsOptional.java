// Java 8+
/**
 * Listing 4.2 — ExceptionVsOptional.java
 * Demonstrates: Contrasting exception-based vs Optional-based API design
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 8+
 */
package chapter04;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionVsOptional {

    private static final Logger logger =
            Logger.getLogger(ExceptionVsOptional.class.getName());

    // Simple User record to represent a domain object
    record User(Long id, String name) {}

    // Custom checked exception — signals unexpected absence
    static class UserNotFoundException extends Exception {
        UserNotFoundException(Long userId) {
            super("User not found with id: " + userId);
        }
    }

    // Simulated data store
    static final Map<Long, User> store = new HashMap<>();
    static {
        store.put(1L, new User(1L, "Alice"));
        store.put(2L, new User(2L, "Bob"));
    }

    // Exception-based — signals that absence is unexpected
    static User loadUserById(Long userId) throws UserNotFoundException {
        User user = store.get(userId);
        if (user == null) {
            throw new UserNotFoundException(userId); // caller MUST handle this
        }
        return user;
    }

    // Optional-based — signals that absence is a normal outcome
    static Optional<User> findUserById(Long userId) {
        return Optional.ofNullable(store.get(userId)); // absence is valid here
    }

    public static void main(String[] args) {
        // Exception-based: caller must handle the checked exception explicitly
        try {
            User user = loadUserById(1L);
            logger.log(Level.INFO, "Loaded: {0}", user.name()); // found
            loadUserById(99L);                             // triggers exception
        } catch (UserNotFoundException e) {
            logger.log(Level.WARNING, "Exception caught: {0}", e.getMessage());
        }

        // Optional-based: absence is expressed as a value, not a signal
        findUserById(2L)
            .ifPresent(u -> logger.log(Level.INFO, "Found: {0}", u.name())); // found

        String result = findUserById(99L)
            .map(User::name)
            .orElse("No user found");                     // absence handled fluently
        logger.log(Level.INFO, "{0}", result);

        // Output:
        // INFO: Loaded: Alice
        // WARNING: Exception caught: User not found with id: 99
        // INFO: Found: Bob
        // INFO: No user found
    }
}
