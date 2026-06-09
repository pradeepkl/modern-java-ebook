// Java 8+
/**
 * Listing 4.1 — ExceptionAsContract.java
 * Demonstrates: Exceptions as part of an API's public contract
 * Chapter 4: Exception Handling the Modern Way
 * Requires: Java 8+
 */
package chapter04;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Represents a simple user domain object. */
class User {
    private final Long id;
    private final String name;

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "'}";
    }
}

/** Checked exception signaling a user was not found — part of the API contract. */
class UserNotFoundException extends Exception {
    public UserNotFoundException(Long userId) {
        super("No user found with ID: " + userId); // descriptive message
    }
}

/** Repository whose method signature declares failure as part of its contract. */
class UserRepository {

    private final Map<Long, User> store = new HashMap<>();

    public UserRepository() {
        store.put(1L, new User(1L, "Alice"));
        store.put(2L, new User(2L, "Bob"));
    }

    // Throws clause makes failure an explicit part of the public API contract
    public User loadUserById(Long userId) throws UserNotFoundException {
        User user = store.get(userId);
        if (user == null) {
            throw new UserNotFoundException(userId); // signal failure intentionally
        }
        return user;
    }
}

public class ExceptionAsContract {

    private static final Logger logger =
            Logger.getLogger(ExceptionAsContract.class.getName());

    public static void main(String[] args) {
        UserRepository repo = new UserRepository();

        // Attempt to load a user that exists
        try {
            User found = repo.loadUserById(1L);
            logger.log(Level.INFO, "Found: {0}", found);
        } catch (UserNotFoundException e) {
            logger.log(Level.WARNING, "Error: {0}", e.getMessage());
        }

        // Attempt to load a user that does NOT exist
        try {
            User missing = repo.loadUserById(99L);
            logger.log(Level.INFO, "Found: {0}", missing);
        } catch (UserNotFoundException e) {
            logger.log(Level.WARNING, "Error: {0}", e.getMessage()); // contract fulfilled
        }

        // Output:
        // INFO: Found: User{id=1, name='Alice'}
        // WARNING: Error: No user found with ID: 99
    }
}
