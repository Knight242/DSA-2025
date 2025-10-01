import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DoublyLinkedListTest {

    @Test
    fun testPushPeekAndPopOperations() {
        val list = DoublyLinkedList<Int>()

        //  empty
        assertTrue(list.isEmpty())
        assertNull(list.peekFront())
        assertNull(list.peekBack())

        // Add elements at front
        list.pushFront(10)          // [10]
        assertEquals(10, list.peekFront())
        assertEquals(10, list.peekBack())
        assertFalse(list.isEmpty())

        list.pushFront(20)          // [20, 10]
        assertEquals(20, list.peekFront())
        assertEquals(10, list.peekBack())

        // Add elements at back
        list.pushBack(5)
        list.pushBack(1)            // [20, 10, 5, 1]
        assertEquals(20, list.peekFront())
        assertEquals(1, list.peekBack())

        // Pop from front
        val front1 = list.popFront() // Removes 20 → [10, 5, 1]
        assertEquals(20, front1)
        assertEquals(10, list.peekFront())

        val front2 = list.popFront() // Removes 10 → [5, 1]
        assertEquals(10, front2)
        assertEquals(5, list.peekFront())

        // Pop from back
        val back1 = list.popBack()   // Remove 1
        assertEquals(1, back1)
        assertEquals(5, list.peekFront())
        assertEquals(5, list.peekBack())

        val back2 = list.popBack()   // Remove 5
        assertEquals(5, back2)
        assertTrue(list.isEmpty())
        assertNull(list.peekFront())
        assertNull(list.peekBack())

        // Popping from empty list
        assertNull(list.popFront())
        assertNull(list.popBack())
    }
}
