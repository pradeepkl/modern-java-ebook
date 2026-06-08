// Java 8+
/**
 * Listing 2.1 — ImperativeVsDeclarative.java
 * Demonstrates: Imperative vs declarative style for filtering collections
 * Chapter 2: Writing Code the Modern Java Way
 * Requires: Java 8+
 */
package chapter02;

import java.util.ArrayList;
import java.util.List;

public class ImperativeVsDeclarative {

    // Simple record representing a user with email and active status
    record User(String email, boolean active) {
        boolean isActive() { return active; }
        String getEmail() { return email; }
    }

    public static void main(String[] args) {
        var users = List.of(
            new User("a@x.com", true),
            new User("b@x.com", false),
            new User("c@x.com", true)
        );

        // Imperative: mechanics obscure intent — manual loop, mutable list, explicit branch
        List<String> emailsImp = new ArrayList<>();
        for (User u : users) {
            if (u.isActive()) {
                emailsImp.add(u.getEmail()); // mutate list inside loop
            }
        }

        // Declarative: pipeline expresses intent — filter, transform, collect
        List<String> emailsDec = users.stream()
            .filter(User::isActive)   // keep only active users
            .map(User::getEmail)      // extract email from each
            .toList();                // collect into immutable list

        System.out.println(emailsImp);
        System.out.println(emailsDec);

        // Output: [a@x.com, c@x.com]
        //         [a@x.com, c@x.com]
    }
}