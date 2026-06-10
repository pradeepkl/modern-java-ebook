// Java 8+
/**
 * Listing 11.4 — ComparatorComposition.java
 * Demonstrates: Declarative Comparator composition with comparing(), thenComparing(), reversed(), nullsFirst()
 * Chapter 11: Declarative Data Transformations
 * Requires: Java 8+
 */
package chapter11;

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

        // ❌ Pre-Java-8 — intent buried in comparison mechanics
        employees.sort(new Comparator<Employee>() {
            @Override
            public int compare(Employee a, Employee b) {
                int dept = a.department().compareTo(b.department());
                if (dept != 0) return dept;
                return Double.compare(b.salary(), a.salary()); // descending salary
            }
        });
        log.info("Pre-Java-8 sort: " + employees);

        // ✅ Declarative — reads as a description of the order
        // Sort by department ascending, then salary descending
        employees.sort(
            Comparator.comparing(Employee::department)
                .thenComparing(
                    Comparator.comparingDouble(Employee::salary)
                              .reversed())); // salary descending within department

        log.info("Declarative sort (dept asc, salary desc): " + employees);

        // nullsFirst and nullsLast — handle absent values gracefully
        List<String> statuses = new ArrayList<>(
            Arrays.asList("SHIPPED", null, "CONFIRMED", null, "PENDING"));

        statuses.sort(
            Comparator.nullsFirst(Comparator.naturalOrder())); // nulls sorted before non-nulls

        log.info("Statuses with nullsFirst: " + statuses);

        // nullsLast — nulls sorted after non-nulls
        statuses.sort(
            Comparator.nullsLast(Comparator.naturalOrder()));

        log.info("Statuses with nullsLast: " + statuses);

        // Output:
        // Declarative sort (dept asc, salary desc):
        //   [Carol/Engineering/110000, Alice/Engineering/95000, Eve/Engineering/95000, Dave/Marketing/85000, Bob/Marketing/72000]
        // Statuses with nullsFirst: [null, null, CONFIRMED, PENDING, SHIPPED]
        // Statuses with nullsLast:  [CONFIRMED, PENDING, SHIPPED, null, null]
    }
}