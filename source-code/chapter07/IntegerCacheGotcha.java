// Java 5+
/**
 * Listing 7.9 — IntegerCacheGotcha.java
 * Demonstrates: The JVM Integer cache for values -128 to 127 and
 *               why == on wrapper types produces surprising results
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 5+
 */
package chapter07;

import java.util.logging.Logger;

public class IntegerCacheGotcha {

    private static final Logger log =
            Logger.getLogger(IntegerCacheGotcha.class.getName());

    public static void main(String[] args) {

        // Values in range [-128, 127] are served from the JVM cache
        Integer a = 100; // autoboxed — returns cached instance
        Integer b = 100; // autoboxed — returns the SAME cached instance
        log.info("100 == 100: " + (a == b)); // true — same object reference

        // Values outside the cache range produce distinct heap objects
        Integer c = 200; // autoboxed — new Integer object allocated
        Integer d = 200; // autoboxed — another new Integer object allocated
        log.info("200 == 200: " + (c == d)); // false — different references

        // Always use equals() to compare wrapper values by content
        log.info("200.equals(200): " + c.equals(d)); // true — value equality

        // Demonstrate the cache boundary explicitly
        Integer lo = 127; // last cached value
        Integer hi = 128; // first uncached value
        Integer lo2 = 127;
        Integer hi2 = 128;
        log.info("127 == 127 (cached):   " + (lo == lo2));  // true
        log.info("128 == 128 (uncached): " + (hi == hi2));  // false
    }

    // Output:
    // INFO: 100 == 100: true
    // INFO: 200 == 200: false
    // INFO: 200.equals(200): true
    // INFO: 127 == 127 (cached):   true
    // INFO: 128 == 128 (uncached): false
}