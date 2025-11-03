import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class HashAssociativeArrayTest {

    @Test
    fun testInsertionAndRetrieval() {
        val map = HashAssociativeArray<String, Int>()
        map["apple"] = 5
        map["banana"] = 10
        assertEquals(5, map["apple"])
        assertEquals(10, map["banana"])
    }

    @Test
    fun testOverwriteExistingKey() {
        val map = HashAssociativeArray<String, Int>()
        map["x"] = 1
        map["x"] = 2
        assertEquals(2, map["x"])
        assertEquals(1, map.size())
    }

    @Test
    fun testContainsKey() {
        val map = HashAssociativeArray<String, Int>()
        map["cat"] = 3
        assertTrue("cat" in map)
        assertFalse("dog" in map)
    }

    @Test
    fun testRemoveKey() {
        val map = HashAssociativeArray<String, Int>()
        map["a"] = 100
        map["b"] = 200
        assertTrue(map.remove("a"))
        assertFalse(map.remove("x"))
        assertNull(map["a"])
    }

    @Test
    fun testRehashing() {
        val map = HashAssociativeArray<Int, String>(3)
        for (i in 0 until 100) {
            map[i] = "val$i"
        }
        assertEquals("val99", map[99])
        assertTrue(map.size() >= 100)
    }

    @Test
    fun testKeyValuePairs() {
        val map = HashAssociativeArray<String, Int>()
        map["one"] = 1
        map["two"] = 2
        val pairs = map.keyValuePairs()
        assertTrue(pairs.contains("one" to 1))
        assertTrue(pairs.contains("two" to 2))
    }
}
