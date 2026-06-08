// Java 8+
/**
 * Listing 2.6 — StrategyPattern.java
 * Demonstrates: Strategy pattern implemented using Function functional interface
 * Chapter 2: Expressing Intent with Modern Java
 * Requires: Java 8+
 */
package chapter02;

import java.util.function.Function;

public class StrategyPattern {

    /**
     * Calculates total amount by applying a tax strategy passed as a Function.
     * The strategy (tax calculation logic) is injected at call time via lambda.
     *
     * @param amount        the base purchase amount
     * @param taxCalculator a Function representing the tax strategy
     * @return the total amount including tax
     */
    public static Double calculateTotalAmount(
            Double amount, Function<Double, Double> taxCalculator) {
        Double tax = taxCalculator.apply(amount); // apply the chosen tax strategy
        return amount + tax;                       // return base + computed tax
    }

    public static void main(String[] args) {
        // Texas tax strategy: 6.25% sales tax
        Function<Double, Double> texasTaxCalculator = amount -> amount * 0.0625;

        // California tax strategy: 7.25% sales tax
        Function<Double, Double> californiaTaxCalculator = amount -> amount * 0.0725;

        // Each call uses a different strategy without changing the core method
        Double texasTotal = calculateTotalAmount(100.0, texasTaxCalculator);
        Double californiaTotal = calculateTotalAmount(100.0, californiaTaxCalculator);

        System.out.println("Total amount in Texas: " + texasTotal);
        System.out.println("Total amount in California: " + californiaTotal);

        // Inline lambda as an anonymous strategy (e.g., no tax)
        Double noTaxTotal = calculateTotalAmount(100.0, amount -> 0.0);
        System.out.println("Total amount with no tax: " + noTaxTotal);

        // Output: Total amount in Texas: 106.25
        //         Total amount in California: 107.25
        //         Total amount with no tax: 100.0
    }
}