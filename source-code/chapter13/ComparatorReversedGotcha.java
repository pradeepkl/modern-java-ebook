// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

/**
 * Listing 13.14 — ComparatorReversedGotcha.java
 * Demonstrates: Comparator.reversed() reverses the entire chain vs. reversing only one clause
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter13;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class ComparatorReversedGotcha {

    private static final Logger LOG =
            Logger.getLogger(ComparatorReversedGotcha.class.getName());

    record Employee(String name, String department, double salary) {}

    void main() {
        List<Employee> employees = List.of(
            new Employee("Alice",   "Engineering", 95000),
            new Employee("Bob",     "Engineering", 72000),
            new Employee("Carol",   "Marketing",   88000),
            new Employee("Dave",    "Marketing",   61000),
            new Employee("Eve",     "Engineering", 83000)
        );

        // NOT IDEAL: reversed() flips both department and salary ordering
        Comparator<Employee> notIdeal =
            Comparator.comparing(Employee::department)
                      .thenComparingDouble(Employee::salary)
                      .reversed();

        LOG.info("NOT IDEAL — entire chain reversed (dept DESC, salary DESC):");
        employees.stream()
                 .sorted(notIdeal)
                 .forEach(e -> LOG.info(
                     String.format("  %-12s %-14s %.0f",
                         e.name(), e.department(), e.salary())));

        // Correct: reversed() applied only to the salary comparator
        Comparator<Employee> correct =
            Comparator.comparing(Employee::department)
                      .thenComparing(
                          Comparator.comparingDouble(Employee::salary).reversed());

        LOG.info("CORRECT — dept ASC, salary DESC within each department:");
        employees.stream()
                 .sorted(correct)
                 .forEach(e -> LOG.info(
                     String.format("  %-12s %-14s %.0f",
                         e.name(), e.department(), e.salary())));

        // Output:
        // NOT IDEAL -- entire chain reversed (dept DESC, salary DESC):
        //   Carol        Marketing      88000
        //   Dave         Marketing      61000
        //   Alice        Engineering    95000
        //   Eve          Engineering    83000
        //   Bob          Engineering    72000
        // CORRECT -- dept ASC, salary DESC within each department:
        //   Alice        Engineering    95000
        //   Eve          Engineering    83000
        //   Bob          Engineering    72000
        //   Carol        Marketing      88000
        //   Dave         Marketing      61000
    }
}