// Java 8+
/**
 * Listing 2.1 — ImperativeVsDeclarative.java
 * Demonstrates: Imperative vs declarative style for filtering collections
 * Chapter 2: Expressing Intent with Modern Java
 * Requires: Java 8+
 */
package chapter02;

import java.util.List;
import java.util.ArrayList;

public class ImperativeVsDeclarative {

    // Compact record representing a user with email and active status
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

        // Imperative: manual iteration with mutable accumulator
        // Reader must trace the loop to understand the intent
        List<String> imperativeEmails = new ArrayList<>();
        for (User user : users) {
            if (user.isActive()) imperativeEmails.add(user.getEmail()); // mutation
        }

        // Declarative: pipeline expresses intent directly
        // filter → map → collect reads like a description of the goal
        List<String> declarativeEmails = users.stream()
                .filter(User::isActive)   // keep only active users
                .map(User::getEmail)      // extract their emails
                .toList();               // collect into an immutable list

        System.out.println("Imperative:  " + imperativeEmails);
        System.out.println("Declarative: " + declarativeEmails);

        // Both produce identical results; declarative version reveals intent faster
        // Output: Imperative:  [alice@example.com, carol@example.com]
        //         Declarative: [alice@example.com, carol@example.com]
    }
}