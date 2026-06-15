// Java 25+
// Feature shown: ZonedDateTime DST gap handling, final in Java 8+

/**
 * Listing 9.1c — DSTTransitions.java
 * Demonstrates: ZonedDateTime behaviour during DST spring-forward gaps
 * and autumn fall-back overlaps in Europe/London
 * Chapter 9: Modern Date and Time
 * Requires: Java 8+ for java.time; Java 25+ for void main() instance
 * main method (JEP 512: Compact Source Files and Instance Main Methods)
 */
package chapter09;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

public class DSTTransitions {

    private static final Logger LOG =
            Logger.getLogger(DSTTransitions.class.getName());

    void main() {
        ZoneId london = ZoneId.of("Europe/London");

        // Spring forward: 2024-03-31 in Europe/London
        // Clocks jump from 01:00 GMT to 02:00 BST
        // 01:30 does not exist on this date — it is in the gap
        ZonedDateTime inGap = ZonedDateTime.of(
                2024, 3, 31, 1, 30, 0, 0, london);
        // java.time adjusts forward automatically to 02:30 BST
        LOG.info("Gap time adjusted: " + inGap);

        // Autumn fall-back: 2024-10-27 in Europe/London
        // Clocks go back from 02:00 BST to 01:00 GMT
        // 01:30 occurs twice — ambiguous overlap
        ZonedDateTime overlap = ZonedDateTime.of(
                2024, 10, 27, 1, 30, 0, 0, london);
        LOG.info("Overlap default:   " + overlap);

        // Resolve ambiguity explicitly — earlier offset = BST (+01:00)
        ZonedDateTime earlierOffset =
                overlap.withEarlierOffsetAtOverlap();
        LOG.info("Earlier offset (BST +01:00): " + earlierOffset);

        // Resolve ambiguity explicitly — later offset = GMT (+00:00)
        ZonedDateTime laterOffset =
                overlap.withLaterOffsetAtOverlap();
        LOG.info("Later offset  (GMT +00:00): " + laterOffset);

        // Confirm these are different instants
        boolean sameInstant = earlierOffset.toInstant()
                .equals(laterOffset.toInstant());
        LOG.info("Same instant? " + sameInstant);

        // Output:
        // Gap time adjusted:           2024-03-31T02:30+01:00[Europe/London]
        // Overlap default:             2024-10-27T01:30+01:00[Europe/London]
        // Earlier offset (BST +01:00): 2024-10-27T01:30+01:00[Europe/London]
        // Later offset  (GMT +00:00):  2024-10-27T01:30Z[Europe/London]
        // Same instant? false
    }
}