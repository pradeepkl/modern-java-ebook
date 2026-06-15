// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 1.2 — InstanceMain.java
 * Demonstrates: instance main method as the program entry point
 * Chapter 1: Modern Java: A Shift in Mindset
 * Requires: Java 25+ (JEP 512: Compact Source Files and Instance Main Methods)
 */
package chapter01;

import java.util.logging.Logger;

public class InstanceMain {

    private static final Logger log =
            Logger.getLogger(InstanceMain.class.getName());

    // Traditional entry point required six tokens of ceremony:
    //   public static void main(String[] args) { ... }
    //
    // The method was static because no instance existed yet.
    // The JVM invoked it directly on the class, not on an object.

    // Modern entry point: an ordinary instance method.
    // Before calling main(), the JVM constructs this object via its
    // no-argument constructor. 'this' is therefore available here.
    void main() {
        // Demonstrate that 'this' is accessible — we are in an instance method
        String className = this.getClass().getSimpleName();

        log.info("Same program. Less ceremony.");
        log.info("Running as an instance of: " + className);

        // Fields initialized before main() runs would also be accessible here
    }

    // Output:
    // INFO: Same program. Less ceremony.
    // INFO: Running as an instance of: InstanceMain
}