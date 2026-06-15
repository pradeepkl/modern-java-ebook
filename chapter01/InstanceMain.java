// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 1.2 — InstanceMain.java
 * Demonstrates: instance main method (void main()) vs traditional static entry point
 * Chapter 1: Modern Java: A Shift in Mindset
 * Requires: Java 25+ (JEP 512: Compact Source Files and Instance Main Methods)
 */
package chapter01;

import java.util.logging.Logger;

public class InstanceMain {

    private static final Logger log =
            Logger.getLogger(InstanceMain.class.getName());

    // A field initialized before main() runs — possible because the JVM
    // constructs an instance of this class before invoking main().
    private final String message = "Same program. Less ceremony.";

    // Traditional entry point required six tokens of ceremony:
    //   public static void main(String[] args) { ... }
    // It is a static method — no instance exists when it runs.

    // Modern: instance main method — the JVM constructs this object first,
    // then calls main() on it. No access modifier, no static, no args.
    void main() {
        // 'this' is available here — we are inside an instance method
        log.info(this.message);

        // Demonstrate that 'this' refers to a real object
        log.info("Entry point class: " + this.getClass().getSimpleName());
    }

    // Output:
    // INFO: Same program. Less ceremony.
    // INFO: Entry point class: InstanceMain
}