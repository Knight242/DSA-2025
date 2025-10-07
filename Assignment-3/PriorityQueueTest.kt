import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PriorityQueueTest {

    @Test
    fun isEmpty() {
        val pq = PriorityQueue<String>()

        // A new queue should be empty
        assertTrue(pq.isEmpty(), "Queue should start empty")

        pq.addWithPriority("Task1", 5.0)
        assertFalse(pq.isEmpty(), "Queue should not be empty after adding an element")

        pq.next()               // remove the element
        assertTrue(pq.isEmpty(), "Queue should be empty again after removing all elements")
    }

    @Test
    fun addWithPriority() {
        val pq = PriorityQueue<String>()

        pq.addWithPriority("Low", 5.0)
        pq.addWithPriority("High", 1.0)

        // Queue should now contain two elements
        assertFalse(pq.isEmpty())
        // The lowest priority value should be removed first
        assertEquals("High", pq.next(), "Element with lowest priority value should come first")
    }

    @Test
    fun next() {
        val pq = PriorityQueue<String>()
        pq.addWithPriority("Low", 5.0)
        pq.addWithPriority("Medium", 3.0)
        pq.addWithPriority("High", 1.0)

        assertEquals("High", pq.next(), "Lowest priority value (1.0) should come out first")
        assertEquals("Medium", pq.next(), "Next lowest (3.0) should follow")
        assertEquals("Low", pq.next(), "Highest (5.0) should come out last")
        assertNull(pq.next(), "Next on an empty queue should return null")
    }

    @Test
    fun adjustPriority() {
        val pq = PriorityQueue<String>()
        pq.addWithPriority("A", 4.0)
        pq.addWithPriority("B", 2.0)
        pq.addWithPriority("C", 3.0)

        // Initial order: B (2.0), C (3.0), A (4.0)
        assertEquals("B", pq.next(), "B should be first with priority 2.0")

        // Make A the highest priority (lowest number)
        pq.adjustPriority("A", 1.0)

        assertEquals("A", pq.next(), "A should move up after lowering its priority value")
        assertEquals("C", pq.next(), "C should come last")
    }
}
