// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+
package chapter02;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Listing 2.1 — ImperativeVsDeclarative.java
 * Demonstrates: imperative loop-based filtering versus an intent-oriented
 * Stream pipeline performing the same transformation on a list of users.
 * Chapter 2: Writing Java the Modern Way
 * Requires: Java 25+ (instance main method via JEP 512)
 */
public class ImperativeVsDeclarative {

    private static final Logger log =
            Logger.getLogger(ImperativeVsDeclarative.class.getName());

    // Simple value type: an email address and an active flag
    record User(String email, boolean active) {}

    void main() {
        var users = List.of(
                new User("alice@example.com", true),
                new User("bob@example.com", false),
                new User("carol@example.com", true));

        // Imperative — mechanics dominate: loop, condition, mutation
        List<String> imperativeEmails = new ArrayList<>();
        for (User user : users) {
            if (user.active()) {                      // guard condition
                imperativeEmails.add(user.email());   // manual accumulation
            }
        }

        // Intent-oriented — outcome is visible: active users, then emails
        List<String> intentEmails = users.stream()
                .filter(User::active)   // keep only active users
                .map(User::email)       // extract the email address
                .toList();              // collect into an unmodifiable list

        log.info("Imperative : " + imperativeEmails);
        log.info("Intent     : " + intentEmails);

        // Output:
        // INFO: Imperative : [alice@example.com, carol@example.com]
        // INFO: Intent     : [alice@example.com, carol@example.com]
    }
}