// Java 25+
// Feature shown: declarative Comparator composition, final in Java 8+

/**
 * Listing 13.6 — ComparatorComposition.java
 * Demonstrates: Declarative Comparator composition using Comparator.comparing,
 * thenComparing, reversed, nullsFirst, and nullsLast
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

    private static final Logger log = Logger.getLogger(ComparatorComposition.class.getName());

    record Employee(String name, String department, double salary) {}

    void main() {
        List<Employee> employees = new ArrayList<>(List.of(
            new Employee("Alice", "Engineering", 95000.0),
            new Employee("Bob",   "Marketing",   72000.0),
            new Employee("Carol", "Engineering", 110000.0),
            new Employee("Dave",  "Marketing",   85000.0),
            new Employee("Eve",   "Engineering", 95000.0)
        ));

        // NOT IDEAL: Pre-Java-8 anonymous Comparator — intent buried in mechanics
        employees.sort(new Comparator<Employee>() {
            @Override
            public int compare(Employee a, Employee b) {
                int dept = a.department().compareTo(b.department());
                if (dept != 0) return dept;
                return Double.compare(b.salary(), a.salary()); // descending salary
            }
        });
        log.info("Anonymous comparator result:");
        employees.forEach(e -> log.info("  " + e.department() + " / " + e.name() + " / " + e.salary()));

        // Declarative — reads as a description of the order
        // Sort by department ascending, then salary descending
        employees.sort(
            Comparator.comparing(Employee::department)
                .thenComparing(
                    Comparator.comparingDouble(Employee::salary)
                        .reversed())); // descending salary within department

        log.info("Declarative comparator result:");
        employees.forEach(e -> log.info("  " + e.department() + " / " + e.name() + " / " + e.salary()));

        // nullsFirst and nullsLast — handle absent values gracefully
        List<String> statuses = new ArrayList<>(
            Arrays.asList("SHIPPED", null, "CONFIRMED", null, "PENDING"));

        statuses.sort(Comparator.nullsFirst(Comparator.naturalOrder())); // nulls sort before non-nulls
        log.info("nullsFirst result: " + statuses);

        statuses.sort(Comparator.nullsLast(Comparator.naturalOrder())); // nulls sort after non-nulls
        log.info("nullsLast result: " + statuses);

        // Output:
        // Anonymous comparator result:
        //   Engineering / Carol / 110000.0
        //   Engineering / Alice / 95000.0
        //   Engineering / Eve   / 95000.0
        //   Marketing   / Dave  / 85000.0
        //   Marketing   / Bob   / 72000.0
        // nullsFirst result: [null, null, CONFIRMED, PENDING, SHIPPED]
        // nullsLast  result: [CONFIRMED, PENDING, SHIPPED, null, null]
    }
}