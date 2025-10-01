import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LinkedListQueueTest {

    @Test
    fun testEnqueueDequeuePeekAndIsEmpty() {
        val queue = LinkedListQueue<String>()

        // empty
        assertTrue(queue.isEmpty())
        assertNull(queue.peek())
        assertNull(queue.dequeue())

        // Enqueue elements
        queue.enqueue("A")
        queue.enqueue("B")
        queue.enqueue("C")

        assertFalse(queue.isEmpty())
        assertEquals("A", queue.peek())       // front = A

        // Dequeue elements
        assertEquals("A", queue.dequeue())    // A → [B, C]
        assertEquals("B", queue.peek())       // now front = B
        assertEquals("B", queue.dequeue())    // removes B
        assertEquals("C", queue.dequeue())    // remove C

        // should be empty again
        assertTrue(queue.isEmpty())
        assertNull(queue.peek())
        assertNull(queue.dequeue())
    }
}
