// Java 8+
package chapter07;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;
import java.util.logging.Logger;

/**
 * Listing 7.3 — PrimitiveFunctionalInterfaces.java
 * Demonstrates: Generic vs primitive-specialised functional interfaces
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 8+
 */
public class PrimitiveFunctionalInterfaces {

    private static final Logger log =
            Logger.getLogger(PrimitiveFunctionalInterfaces.class.getName());

    public static void main(String[] args) {

        // ❌ Function<Integer, Integer>
        // Boxes both input and output on every call
        Function<Integer, Integer> doubleBoxed =
                n -> n * 2;

        // ✅ IntUnaryOperator — int in, int out, no boxing
        IntUnaryOperator doublePrimitive =
                n -> n * 2;

        // ✅ IntFunction<String> — int in, reference out, no unboxing needed
        IntFunction<String> describe =
                n -> "quantity: " + n;

        // ✅ ToIntFunction<String> — reference in, int out, no boxing of result
        ToIntFunction<String> parse =
                Integer::parseInt;

        // Boxed: Integer.valueOf(5) created on input, Integer.valueOf(10) on output
        log.info("doubleBoxed(5)        = " + doubleBoxed.apply(5));

        // Primitive: no allocation, pure int arithmetic
        log.info("doublePrimitive(5)    = " + doublePrimitive.applyAsInt(5));

        // int in, String out — only one side is a reference type
        log.info("describe(5)           = " + describe.apply(5));

        // String in, int out — result stays primitive
        log.info("parse(\"42\")           = " + parse.applyAsInt("42"));

        // Output:
        // doubleBoxed(5)        = 10
        // doublePrimitive(5)    = 10
        // describe(5)           = quantity: 5
        // parse("42")           = 42
    }
}