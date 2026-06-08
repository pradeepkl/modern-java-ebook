// Java 8+
/**
 * Listing 2.6 — StrategyPattern.java
 * Demonstrates: Strategy pattern using Function functional interface
 * Chapter 2: Expressing Intent with Modern Java
 * Requires: Java 8+
 */
package chapter02;

import java.util.function.Function;

public class StrategyPattern {

    // Strategy pattern: behavior passed as a Function argument
    public static Double calculateTotalAmount(
            Double amount, Function<Double, Double> taxCalculator) {
        Double tax = taxCalculator.apply(amount); // apply the chosen tax strategy
        return amount + tax;                       // return amount plus calculated tax
    }

    public static void main(String[] args) {
        // Each lambda represents a different tax strategy
        Function<Double, Double> texasTaxCalculator =
                amount -> amount * 0.0625;         // Texas: 6.25% tax rate

        Function<Double, Double> californiaTaxCalculator =
                amount -> amount * 0.0725;         // California: 7.25% tax rate

        Function<Double, Double> noTaxCalculator =
                amount -> 0.0;                     // No-tax strategy (e.g., exempt items)

        // Pass different strategies to the same method
        Double texasTotal      = calculateTotalAmount(100.0, texasTaxCalculator);
        Double californiaTotal = calculateTotalAmount(100.0, californiaTaxCalculator);
        Double noTaxTotal      = calculateTotalAmount(100.0, noTaxCalculator);

        System.out.println("Total amount in Texas: "      + texasTotal);
        System.out.println("Total amount in California: " + californiaTotal);
        System.out.println("Total amount (tax-exempt): "  + noTaxTotal);

        // Output: Total amount in Texas: 106.25
        //         Total amount in California: 107.25
        //         Total amount (tax-exempt): 100.0
    }
}