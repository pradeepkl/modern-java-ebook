// Java 8+
/**
 * Listing 2.1 — ImperativeVsDeclarative.java
 * Demonstrates: Contrasting imperative loop-based filtering with declarative Stream pipelines
 * Chapter 2: Expressing Intent with Modern Java
 * Requires: Java 8+
 */
package chapter02;

import java.util.List;
import java.util.ArrayList;

public class ImperativeVsDeclarative {

    // Compact record representing a system user with email and active status
    record User(String email, boolean active) {
        boolean isActive() { return active; }
        String getEmail() { return email; }
    }

    public static void main(String[] args) {
        var users = List.of(
            new User("alice@example.com", true),
            new User("bob@example.com", false),
            new User("carol@example.com", true)
        );

        // Imperative: manual iteration with mutable accumulator — intent is buried in mechanics
        List<String> imperativeEmails = new ArrayList<>();
        for (User user : users) {
            if (user.isActive()) {
                imperativeEmails.add(user.getEmail()); // mutation hidden inside loop body
            }
        }

        // Declarative: pipeline expresses intent directly — filter, then map, then collect
        List<String> declarativeEmails = users.stream()
                .filter(User::isActive)   // keep only active users
                .map(User::getEmail)      // extract email addresses
                .toList();                // collect into an unmodifiable list (Java 16+)

        System.out.println("Imperative  : " + imperativeEmails);
        System.out.println("Declarative : " + declarativeEmails);

        // Both approaches produce identical results; declarative reads closer to the domain
        boolean equivalent = imperativeEmails.equals(declarativeEmails);
        System.out.println("Results match: " + equivalent);

        // Output:
        // Imperative  : [alice@example.com, carol@example.com]
        // Declarative : [alice@example.com, carol@example.com]
        // Results match: true
    }
}