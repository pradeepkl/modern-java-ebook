// Java 5+
/**
 * Listing 2.10 — VarargsExample.java
 * Demonstrates: Variable-length arguments (varargs) for flexible method signatures
 * Chapter 2: Writing Code the Modern Java Way
 * Requires: Java 5+
 */
package chapter02;

public class VarargsExample {

    // Accepts any number of int arguments via varargs (int... becomes int[])
    public static int sum(int... numbers) {
        int total = 0;
        for (int number : numbers) {
            total += number; // accumulate each value into running total
        }
        return total;
    }

    // Varargs also works with objects — accepts zero or more String arguments
    public static String join(String separator, String... words) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i > 0) sb.append(separator); // add separator between words
            sb.append(words[i]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // Call with different numbers of arguments — no array creation needed
        System.out.println(sum(1, 2, 3));           // three arguments
        System.out.println(sum(10, 20, 30, 40));    // four arguments
        System.out.println(sum());                  // zero arguments — valid

        // Varargs with String type
        System.out.println(join(", ", "Alice", "Bob", "Charlie"));
        System.out.println(join("-", "Java", "5", "Plus"));

        // Output: 6
        //         100
        //         0
        //         Alice, Bob, Charlie
        //         Java-5-Plus
    }
}