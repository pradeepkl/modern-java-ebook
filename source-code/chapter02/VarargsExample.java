// Java 5+
/**
 * Listing 2.10 — VarargsExample.java
 * Demonstrates: Varargs for flexible argument counts in expressive API design
 * Chapter 2: Expressing Intent with Modern Java
 * Requires: Java 5+
 */
package chapter02;

public class VarargsExample {

    /**
     * Accepts any number of int arguments using varargs syntax.
     * The compiler treats the parameter as an int[] internally.
     */
    public static int sum(int... numbers) {
        int total = 0;
        for (int number : numbers) { // iterate over varargs like an array
            total += number;
        }
        return total;
    }

    /**
     * Concatenates any number of String values with a separator.
     * Demonstrates varargs with non-primitive types.
     */
    public static String join(String separator, String... parts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                sb.append(separator); // insert separator between elements
            }
            sb.append(parts[i]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // Caller passes any number of ints naturally — no array syntax needed
        System.out.println(sum(1, 2, 3));         // three arguments
        System.out.println(sum(10, 20, 30, 40));  // four arguments
        System.out.println(sum());                 // zero arguments — valid

        // Varargs also work with object types
        System.out.println(join(", ", "Alice", "Bob", "Charlie"));
        System.out.println(join("-", "2024", "01", "15"));

        // Explicit array can also be passed to a varargs method
        int[] explicit = {5, 10, 15};
        System.out.println(sum(explicit)); // array passed directly

        // Output:
        // 6
        // 100
        // 0
        // Alice, Bob, Charlie
        // 2024-01-15
        // 30
    }
}