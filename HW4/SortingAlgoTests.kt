import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SortingAlgoTests {

    private fun <T : Comparable<T>> isSorted(list: List<T>): Boolean {
        for (i in 0 until list.size - 1) {
            if (list[i] > list[i + 1]) return false
        }
        return true
    }

    @Test
    fun testInsertionSortEmptyList() {
        val data = mutableListOf<Int>()
        insertionSort(data)
        assertTrue(isSorted(data), "Empty list should stay sorted")
    }

    @Test
    fun testInsertionSortSmallList() {
        val data = mutableListOf(5, 2, 9, 1, 3)
        insertionSort(data)
        assertEquals(listOf(1, 2, 3, 5, 9), data)
    }

    @Test
    fun testSelectionSortSmallList() {
        val data = mutableListOf(8, 4, 7, 2)
        selectionSort(data)
        assertEquals(listOf(2, 4, 7, 8), data)
    }

    @Test
    fun testMergeSortSmallList() {
        val data = mutableListOf(10, -1, 5, 2)
        val result = mergeSort(data)
        assertEquals(listOf(-1, 2, 5, 10), result)
    }

    @Test
    fun testHeapSortSmallList() {
        val data = mutableListOf(3, 1, 4, 1, 5)
        heapSort(data)
        assertEquals(listOf(1, 1, 3, 4, 5), data)
    }

    @Test
    fun testLargeRandomListAllAlgorithms() {
        val original = List(1_000) { (0..10_000).random() }

        val copy1 = original.toMutableList()
        insertionSort(copy1)
        assertTrue(isSorted(copy1), "Insertion Sort failed on large input")

        val copy2 = original.toMutableList()
        selectionSort(copy2)
        assertTrue(isSorted(copy2), "Selection Sort failed on large input")

        val copy3 = original.toMutableList()
        val mergeResult = mergeSort(copy3)
        assertTrue(isSorted(mergeResult), "Merge Sort failed on large input")

        val copy4 = original.toMutableList()
        heapSort(copy4)
        assertTrue(isSorted(copy4), "Heap Sort failed on large input")
    }

    @Test
    fun testAlreadySortedList() {
        val data = mutableListOf(1, 2, 3, 4, 5)
        insertionSort(data)
        assertTrue(isSorted(data), "Algorithm should leave sorted list unchanged")
    }

    @Test
    fun testListWithDuplicates() {
        val data = mutableListOf(2, 3, 2, 1, 3)
        heapSort(data)
        assertTrue(isSorted(data), "Heap Sort should handle duplicates correctly")
    }
}
