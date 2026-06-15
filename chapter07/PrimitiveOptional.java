// Java 25+
// Feature shown: primitive optional variants (OptionalInt, OptionalDouble), final in Java 8+

/**
 * Listing 7.8 — PrimitiveOptional.java
 * Demonstrates: OptionalInt and OptionalDouble as boxing-free absence types
 * Chapter 7: Primitive Types, Boxing, and the Cost of Abstraction
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter07;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.logging.Logger;

public class PrimitiveOptional {

    private static final Logger log =
            Logger.getLogger(PrimitiveOptional.class.getName());

    // Processes an int[] — no boxing at any stage
    static void processReadings(int[] readings) {

        // OptionalInt — no boxing at any point
        OptionalInt maxReading = IntStream
                .of(readings)
                .filter(r -> r > 100)
                .max();

        // orElse returns int — no unboxing needed
        int threshold = maxReading.orElse(0);

        // ifPresent receives an IntConsumer — still no boxing
        maxReading.ifPresent(max ->
                log.info("Max reading: " + max));

        log.info("Threshold: " + threshold);
    }

    // OptionalDouble example — average of sensor values
    static void processSensorAverage(int[] sensors) {

        // average() returns OptionalDouble — primitive specialisation
        OptionalDouble avg = IntStream
                .of(sensors)
                .filter(s -> s >= 0)
                .average();

        // orElse on OptionalDouble returns double — no unboxing
        double result = avg.orElse(Double.NaN);
        log.info("Average sensor value: " + result);
    }

    void main() {
        int[] readings = {95, 110, 87, 130, 45, 120};
        processReadings(readings);

        int[] allBelowThreshold = {50, 60, 70};
        processReadings(allBelowThreshold); // absent — orElse(0) used

        int[] sensors = {10, 20, 30, 40};
        processSensorAverage(sensors);

        // Output:
        // INFO: Max reading: 130
        // INFO: Threshold: 130
        // INFO: Threshold: 0
        // INFO: Average sensor value: 25.0
    }
}