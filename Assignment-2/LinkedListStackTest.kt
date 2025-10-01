import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LinkedListStackTest {

    @Test
    fun testPushPopPeekAndIsEmpty() {
        val stack = LinkedListStack<Int>()

        // empty
        assertTrue(stack.isEmpty())
        assertNull(stack.peek())
        assertNull(stack.pop())

        // Push elements
        stack.push(10)      // [10]
        stack.push(20)      // [20, 10]
        stack.push(30)      // [30, 20, 10]

        assertFalse(stack.isEmpty())
        assertEquals(30, stack.peek())  // top is 30
        assertEquals(30, stack.pop())   // removes 30
        assertEquals(20, stack.peek())  // new top is 20

        // Pop  elements
        assertEquals(20, stack.pop())   // removes 20
        assertEquals(10, stack.pop())   // removes 10

        // Stack should be empty again
        assertTrue(stack.isEmpty())
        assertNull(stack.peek())
        assertNull(stack.pop())
    }
}
