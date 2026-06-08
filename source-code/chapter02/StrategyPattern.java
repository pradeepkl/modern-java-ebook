// Java 8+
/**
 * Listing 2.6 — StrategyPattern.java
 * Demonstrates: Strategy pattern using higher-order functions and lambdas
 * Chapter 2: Writing Code the Modern Java Way
 * Requires: Java 8+
 */
package chapter02;

import java.util.function.Function;

public class StrategyPattern {

    // Tax strategies defined as lambda expressions (behavior stored as data)
    static Function<Double, Double> texasTaxCalculator =
            amount -> amount * 0.0625; // Texas tax rate: 6.25%

    static Function<Double, Double> californiaTaxCalculator =
            amount -> amount * 0.0725; // California tax rate: 7.25%

    /**
     * Accepts a tax strategy as an argument — Strategy pattern via higher-order function.
     * The caller decides which tax calculation behavior to inject at runtime.
     *
     * @param amount        the base purchase amount
     * @param taxCalculator the tax strategy to apply
     * @return total amount including tax
     */
    public static Double calculateTotalAmount(
            Double amount,
            Function<Double, Double> taxCalculator) {

        Double tax = taxCalculator.apply(amount); // Apply the injected strategy
        return amount + tax;                       // Return base + computed tax
    }

    public static void main(String[] args) {
        // Pass different strategies to the same method — no subclassing needed
        Double texasTotal = calculateTotalAmount(100.0, texasTaxCalculator);
        Double californiaTotal = calculateTotalAmount(100.0, californiaTaxCalculator);

        System.out.println("Total amount in Texas: " + texasTotal);
        System.out.println("Total amount in California: " + californiaTotal);

        // Inline lambda as an ad-hoc strategy — no need to declare a named field
        Double customTotal = calculateTotalAmount(100.0, amount -> amount * 0.08);
        System.out.println("Total amount with custom 8% tax: " + customTotal);

        // Output:
        // Total amount in Texas: 106.25
        // Total amount in California: 107.25
        // Total amount with custom 8% tax: 108.0
    }
}