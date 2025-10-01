import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ReverseStackTest {

    @Test
    fun testReverseStack() {
        val stack = LinkedListStack<Int>()
        stack.push(1)
        stack.push(2)
        stack.push(3)     // top = 3

        reverseStack(stack)

        // Now the order is reversed: top → 1 → 2 → 3
        assertEquals(1, stack.pop())
        assertEquals(2, stack.pop())
        assertEquals(3, stack.pop())
        assertTrue(stack.isEmpty())
    }
}
