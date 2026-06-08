// Java 16+
/**
 * Listing 3.11 — PersonRecord.java
 * Demonstrates: Records as immutable data carriers with auto-generated
 *               accessors, equals, hashCode, toString, and custom methods
 * Chapter 3: Inheritance Reimagined
 * Requires: Java 16+
 */
package chapter03;

// Record declaration: components become final fields automatically
public record PersonRecord(String name, int age) {

    // Compact canonical constructor for validation
    public PersonRecord {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Age must be non-negative");
        }
    }

    // Custom instance method — records can define behavior
    public String greet() {
        return "Hello, my name is " + name
                + " and I am " + age + " years old.";
    }

    public static void main(String[] args) {
        // Records are instantiated like regular classes
        PersonRecord alice = new PersonRecord("Alice", 30);
        PersonRecord bob   = new PersonRecord("Bob", 25);

        // Auto-generated accessor methods (not getters — just component names)
        System.out.println("Name : " + alice.name());  // accessor for name
        System.out.println("Age  : " + alice.age());   // accessor for age

        // Auto-generated toString()
        System.out.println(alice);   // PersonRecord[name=Alice, age=30]

        // Auto-generated equals() compares component values
        PersonRecord alice2 = new PersonRecord("Alice", 30);
        System.out.println("Equal: " + alice.equals(alice2)); // true

        // Auto-generated hashCode() consistent with equals()
        System.out.println("Same hash: " + (alice.hashCode() == alice2.hashCode()));

        // Custom method
        System.out.println(alice.greet());
        System.out.println(bob.greet());

        // Output:
        // Name : Alice
        // Age  : 30
        // PersonRecord[name=Alice, age=30]
        // Equal: true
        // Same hash: true
        // Hello, my name is Alice and I am 30 years old.
        // Hello, my name is Bob and I am 25 years old.
    }
}