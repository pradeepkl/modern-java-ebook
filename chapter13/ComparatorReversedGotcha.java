// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+

package chapter13;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Listing 13.14 — ComparatorReversedGotcha.java
 * Demonstrates: Comparator.reversed() reverses the entire chain, not just the
 * last thenComparing clause, and shows the correct approach to reverse only
 * the salary portion of a comparator.
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
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

        // NOT IDEAL: reversed() flips both department AND salary ordering
        Comparator<Employee> notIdeal =
            Comparator.comparing(Employee::department)
                      .thenComparingDouble(Employee::salary)
                      .reversed(); // reverses the entire chain

        List<Employee> notIdealSorted = employees.stream()
                .sorted(notIdeal)
                .toList();

        LOG.info("NOT IDEAL (both department and salary reversed):");
        notIdealSorted.forEach(e ->
            LOG.info(e.department() + " | " + e.name() + " | " + e.salary()));

        // Correct: reversed() applied only to the inner salary comparator
        Comparator<Employee> correct =
            Comparator.comparing(Employee::department)
                      .thenComparing(
                          Comparator.comparingDouble(Employee::salary)
                                    .reversed()); // reverses salary only

        List<Employee> correctSorted = employees.stream()
                .sorted(correct)
                .toList();

        LOG.info("CORRECT (department ascending, salary descending):");
        correctSorted.forEach(e ->
            LOG.info(e.department() + " | " + e.name() + " | " + e.salary()));

        // Output:
        // NOT IDEAL (both department and salary reversed):
        // Marketing | Carol | 88000.0
        // Marketing | Dave  | 61000.0
        // Engineering | Alice | 95000.0
        // Engineering | Eve   | 83000.0
        // Engineering | Bob   | 72000.0
        // CORRECT (department ascending, salary descending):
        // Engineering | Alice | 95000.0
        // Engineering | Eve   | 83000.0
        // Engineering | Bob   | 72000.0
        // Marketing | Carol | 88000.0
        // Marketing | Dave  | 61000.0
    }
}