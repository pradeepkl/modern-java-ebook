// Java 25+
// Feature shown: declarative Comparator composition, final in Java 8+

/**
 * Listing 13.6 — ComparatorComposition.java
 * Demonstrates: Comparator.comparing, thenComparing, reversed, nullsFirst
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 25+ (compiled with --enable-preview --release 21 for
 * the void main() instance main method)
 */
package chapter13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class ComparatorComposition {

    private static final Logger log =
            Logger.getLogger(ComparatorComposition.class.getName());

    record Employee(String department, double salary) {}

    void main() {

        List<Employee> employees = new ArrayList<>(List.of(
                new Employee("Engineering", 95000.0),
                new Employee("Marketing",  72000.0),
                new Employee("Engineering", 88000.0),
                new Employee("Marketing",  81000.0),
                new Employee("Design",     76000.0)
        ));

        // NOT IDEAL: anonymous Comparator — intent buried in mechanics
        employees.sort(new Comparator<Employee>() {
            @Override
            public int compare(Employee a, Employee b) {
                int dept = a.department().compareTo(b.department());
                if (dept != 0) return dept;
                return Double.compare(b.salary(), a.salary()); // desc
            }
        });
        log.info("Anonymous comparator result:");
        employees.forEach(e ->
                log.info("  " + e.department() + " / " + e.salary()));

        // Declarative — reads as a description of the order
        // Sort by department ascending, then salary descending
        employees.sort(
                Comparator.comparing(Employee::department)
                        .thenComparing(
                                Comparator.comparingDouble(
                                        Employee::salary)
                                        .reversed()));

        log.info("Declarative comparator result:");
        employees.forEach(e ->
                log.info("  " + e.department() + " / " + e.salary()));

        // nullsFirst and nullsLast — handle absent values
        List<String> statuses = new ArrayList<>(
                Arrays.asList("SHIPPED", null, "CONFIRMED", null, "PENDING"));

        statuses.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        log.info("nullsFirst result: " + statuses);
        // Output: [null, null, CONFIRMED, PENDING, SHIPPED]
    }
}