// Java 25+
// Feature shown: Integer cache and == vs equals() for wrapper types, final in Java 5+

/**
 * Listing 7.9 — IntegerCacheGotcha.java
 * Demonstrates: The JVM Integer cache for values -128 to 127 and why == on
 * wrapper types produces surprising results outside that range.
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 25+ (instance main method via JEP 512)
 */
package chapter07;

import java.util.logging.Logger;

public class IntegerCacheGotcha {

    private static final Logger log =
            Logger.getLogger(IntegerCacheGotcha.class.getName());

    void main() {
        // Values in the cached range -128 to 127: same object returned
        Integer a = 100;
        Integer b = 100;
        log.info("100 == 100: " + (a == b));   // true — cached instance

        // Values outside the cached range: distinct objects allocated
        Integer c = 200;
        Integer d = 200;
        log.info("200 == 200: " + (c == d));   // false — not cached

        // equals() compares value, not object identity — always correct
        log.info("200.equals(200): " + c.equals(d)); // true

        // Demonstrate the boundary explicitly
        Integer lo = 127;
        Integer hi = 128;
        Integer lo2 = 127;
        Integer hi2 = 128;
        log.info("127 == 127 (boundary, cached): " + (lo == lo2));   // true
        log.info("128 == 128 (boundary, not cached): " + (hi == hi2)); // false

        // Output:
        // INFO: 100 == 100: true
        // INFO: 200 == 200: false
        // INFO: 200.equals(200): true
        // INFO: 127 == 127 (boundary, cached): true
        // INFO: 128 == 128 (boundary, not cached): false
    }
}