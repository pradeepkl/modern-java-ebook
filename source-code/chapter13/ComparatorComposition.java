// Java 8+
/**
 * Listing 13.6 — ComparatorComposition.java
 * Demonstrates: Declarative Comparator composition using comparing(), thenComparing(), reversed(), nullsFirst()
 * Chapter 13: Declarative Data Transformations
 * Requires: Java 8+
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

    public static void main(String[] args) {

        List<Employee> employees = new ArrayList<>(Arrays.asList(
            new Employee("Alice",   "Engineering", 95000),
            new Employee("Bob",     "Marketing",   72000),
            new Employee("Carol",   "Engineering", 110000),
            new Employee("Dave",    "Marketing",   85000),
            new Employee("Eve",     "Engineering", 95000)
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
        log.info("Anonymous comparator result: " + employees);

        // Declarative — reads as a description of the order
        // Sort by department ascending, then salary descending
        employees.sort(
            Comparator.comparing(Employee::department)
                .thenComparing(
                    Comparator.comparingDouble(Employee::salary)
                              .reversed())); // descending salary within department

        employees.forEach(e ->
            log.info(e.department() + " | " + e.name() + " | " + e.salary()));

        // nullsFirst and nullsLast — handle absent values gracefully
        List<String> statuses = new ArrayList<>(
            Arrays.asList("SHIPPED", null, "CONFIRMED", null, "PENDING"));

        statuses.sort(
            Comparator.nullsFirst(
                Comparator.naturalOrder())); // nulls sorted before non-null values

        log.info("Nulls-first sorted statuses: " + statuses);
        // Output: [null, null, CONFIRMED, PENDING, SHIPPED]
    }
}