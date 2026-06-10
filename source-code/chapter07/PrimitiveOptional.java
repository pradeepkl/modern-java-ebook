// Java 8+
/**
 * Listing 7.8 — PrimitiveOptional.java
 * Demonstrates: Primitive-specialised Optional variants (OptionalInt, OptionalDouble)
 * to avoid boxing overhead in numeric computations.
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 8+
 */
package chapter07;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.logging.Logger;

public class PrimitiveOptional {

    private static final Logger log =
            Logger.getLogger(PrimitiveOptional.class.getName());

    // Process sensor readings — all primitive, no boxing at any point
    public static void processReadings(int[] readings) {

        // OptionalInt — no boxing at any point
        OptionalInt maxReading = IntStream
                .of(readings)
                .filter(r -> r > 100)   // predicate on primitive int
                .max();                  // returns OptionalInt, not Optional<Integer>

        // orElse returns int — no unboxing needed
        int threshold = maxReading.orElse(0);

        // ifPresent receives an IntConsumer — lambda takes primitive int
        maxReading.ifPresent(max ->
                log.info("Max reading above 100: " + max));

        log.info("Threshold: " + threshold);
    }

    public static void processTemperatures(double[] temps) {

        // OptionalDouble — specialised for double primitives
        OptionalDouble average = IntStream
                .range(0, temps.length)
                .mapToDouble(i -> temps[i])  // DoubleStream, no boxing
                .filter(t -> t > 0.0)
                .average();                   // returns OptionalDouble

        // getAsDouble — returns primitive double, no unboxing
        double result = average.orElse(Double.NaN);
        log.info("Average positive temperature: " + result);
    }

    public static void main(String[] args) {

        int[] sensorReadings = {85, 102, 97, 115, 88, 130, 45};
        processReadings(sensorReadings);

        // Empty case — no values pass the filter
        int[] lowReadings = {50, 60, 70};
        processReadings(lowReadings);   // threshold falls back to orElse(0)

        double[] temperatures = {-3.5, 18.2, 22.7, -1.0, 15.4};
        processTemperatures(temperatures);

        // Output:
        // INFO: Max reading above 100: 130
        // INFO: Threshold: 130
        // INFO: Threshold: 0
        // INFO: Average positive temperature: 18.766666666666666
    }
}