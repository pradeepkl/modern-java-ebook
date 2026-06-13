// Java 21+ (preview — instance main methods, JEP 445/463)
// Feature shown: var (type inference), final in Java 10+

/**
 * Listing 1.1 — VarTypeInference.java
 * Demonstrates: var keyword for local variable type inference
 * Chapter 1: Modern Java: A Shift in Mindset
 * Requires: Java 10+ for var; compiled with --enable-preview --release 21 for
 * the void main() instance main method
 */
package chapter01;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class VarTypeInference {

    private static final Logger log =
            Logger.getLogger(VarTypeInference.class.getName());

    void main() {

        // Before var: type declared redundantly on both sides
        Map<String, List<Integer>> verbose =
                new HashMap<String, List<Integer>>();

        // var lets the compiler infer the type from the initializer
        var scores = new HashMap<String, List<Integer>>();  // inferred: HashMap<String, List<Integer>>
        var name = "Alice";       // inferred: String
        var threshold = 42;       // inferred: int

        scores.put(name, List.of(threshold, 85, 91));

        // verbose and scores are the same type; var is still statically typed
        verbose.put("control", List.of(0));

        // Wrong type would cause a compile error — type safety is preserved
        log.info(name + ": " + scores.get(name));

        // Output: Alice: [42, 85, 91]
    }
}