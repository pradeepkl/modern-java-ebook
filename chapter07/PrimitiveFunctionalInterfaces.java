// Java 25+
// Feature shown: primitive functional interfaces (IntUnaryOperator, IntFunction), final in Java 8+
/**
 * Listing 7.3 — PrimitiveFunctionalInterfaces.java
 * Demonstrates: primitive-specialised functional interfaces vs generic boxed equivalents
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter07;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;
import java.util.logging.Logger;

public class PrimitiveFunctionalInterfaces {

    private static final Logger log =
            Logger.getLogger(
                PrimitiveFunctionalInterfaces.class.getName());

    void main() {

        // NOT IDEAL: Function<Integer, Integer> boxes both input and output
        Function<Integer, Integer> doubleBoxed =
                n -> n * 2;

        // Preferred: IntUnaryOperator — int in, int out, no boxing at all
        IntUnaryOperator doublePrimitive =
                n -> n * 2;

        // Preferred: IntFunction<String> — primitive int in, reference type out
        IntFunction<String> describe =
                n -> "quantity: " + n;

        // Preferred: ToIntFunction<String> — reference type in, primitive int out
        ToIntFunction<String> parse =
                Integer::parseInt;

        // Apply each interface and log results
        log.info("doubleBoxed(5)       = " + doubleBoxed.apply(5));
        log.info("doublePrimitive(5)   = " + doublePrimitive.applyAsInt(5));
        log.info("describe(5)          = " + describe.apply(5));
        log.info("parse(\"42\")          = " + parse.applyAsInt("42"));

        // Output:
        // doubleBoxed(5)       = 10
        // doublePrimitive(5)   = 10
        // describe(5)          = quantity: 5
        // parse("42")          = 42
    }
}