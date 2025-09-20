import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MadLibPromptTest {

    @Test
    fun madLibPrompt() {
        // ⚠️ Hard to test interactively because it uses readLine().
        // You can simulate input by redirecting System.`in`, like this:
        val input = "dog\n"
        val originalIn = System.`in`
        try {
            System.setIn(input.byteInputStream())
            val result = madLibPrompt(Pair(0, "animal"))
            assertEquals("DOG", result)
        } finally {
            System.setIn(originalIn)
        }
    }

    @Test
    fun enterMadLibs() {
        // Similar to madLibPrompt, test with fake input
        val input = "42\ncats\n"
        val originalIn = System.`in`
        try {
            System.setIn(input.byteInputStream())
            val dict = mutableMapOf(
                Pair(0, "number") to "",
                Pair(1, "plural noun") to ""
            )
            enterMadLibs(dict)
            assertEquals("42", dict[Pair(0, "number")])
            assertEquals("CATS", dict[Pair(1, "plural noun")])
        } finally {
            System.setIn(originalIn)
        }
    }

    @Test
    fun generateStory() {
        val storyTemplate = "You have <0-number> <1-plural noun>?!"
        val dict = mapOf(
            Pair(0, "number") to "42",
            Pair(1, "plural noun") to "CATS"
        )
        val result = generateStory(storyTemplate, dict.toMutableMap())
        assertEquals("You have 42 CATS?!", result)
    }

    @Test
    fun isPalindrome() {
        assertTrue(isPalindrome("ABBA"))
        assertTrue(isPalindrome("A"))
        assertTrue(isPalindrome(""))
        assertFalse(isPalindrome("HELLO"))
    }

    @Test
    fun power() {
        assertEquals(8, power(2, 3))
        assertEquals(1, power(7, 0))
    }

    @Test
    fun factorial() {
        assertEquals(120, factorial(5))
        assertEquals(1, factorial(0))
    }

    @Test
    fun testReverseList() {
        assertEquals(listOf(3, 2, 1), reverseList(listOf(1, 2, 3)))
        assertEquals(emptyList<Int>(), reverseList(emptyList<Int>())) // ✅ specify <Int>
    }

    @Test
    fun makeSalesTax() {
        val addTax = makeSalesTax(0.1)
        assertEquals(110.0, addTax(100.0), 1e-9) // ✅ fixed
    }

    @Test
    fun testCompose() {
        val f = { x: Int -> x + 1 }
        val g = { x: Int -> x * 2 }
        val composed = compose<Int, Int, Int>(f, g) // ✅ specify type parameters
        assertEquals(11, composed(5))
    }

    @Test
    fun linearSearch() {
        val list = listOf(10, 20, 30, 40)
        assertEquals(1, linearSearch(list, 20))
        assertNull(linearSearch(list, 99))
    }

    @Test
    fun binarySearch() {
        val list = listOf(1, 3, 5, 7, 9)
        assertEquals(2, binarySearch(list, 5))
        assertEquals(4, binarySearch(list, 9))
        assertNull(binarySearch(list, 2))
    }
}
