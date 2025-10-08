import kotlin.random.Random
import kotlin.time.measureTime
import kotlin.time.DurationUnit

// InsertionSort
fun insertionSort(array: MutableList<Int>) {
    for (i in 1 until array.size) {
        val key = array[i]
        var j = i - 1
        while (j >= 0 && array[j] > key) {
            array[j + 1] = array[j]
            j--
        }
        array[j + 1] = key
    }
}

// SelectionSort
fun selectionSort(array: MutableList<Int>) {
    for (i in 0 until array.size - 1) {
        var minIdx = i
        for (j in i + 1 until array.size) {
            if (array[j] < array[minIdx]) minIdx = j
        }
        val temp = array[i]
        array[i] = array[minIdx]
        array[minIdx] = temp
    }
}

// MergeSort
fun mergeSort(array: MutableList<Int>): MutableList<Int> {
    if (array.size <= 1) return array
    val mid = array.size / 2
    val left = mergeSort(array.subList(0, mid).toMutableList())
    val right = mergeSort(array.subList(mid, array.size).toMutableList())
    return merge(left, right)
}

private fun merge(left: MutableList<Int>, right: MutableList<Int>): MutableList<Int> {
    val result = mutableListOf<Int>()
    var i = 0
    var j = 0
    while (i < left.size && j < right.size) {
        if (left[i] <= right[j]) {
            result.add(left[i])
            i++
        }
        else {
            result.add(right[j])
            j++
        }
    }
    while (i < left.size) result.add(left[i++])
    while (j < right.size) result.add(right[j++])
    return result
}

// HeapSort
fun heapSort(array: MutableList<Int>) {
    fun heap(n: Int, i: Int) {
        var largest = i
        val l = 2 * i + 1
        val r = 2 * i + 2

        if (l < n && array[l] > array[largest]) largest = l
        if (r < n && array[r] > array[largest]) largest = r

        if (largest != i) {
            val swap = array[i]
            array[i] = array[largest]
            array[largest] = swap
            heap(n, largest)
        }
    }

    val n = array.size
    for (i in n / 2 - 1 downTo 0) heap(n, i)
    for (i in n - 1 downTo 0) {
        val temp = array[0]
        array[0] = array[i]
        array[i] = temp
        heap(i, 0)
    }
}


fun main() {
    val listsizes = listOf(10, 100, 1_000, 10_000, 100_000)

    for (size in listsizes) {
        val original = List(size) { Random.nextInt(0, 100000) }
        println("\n--- List Size: $size ---")

        val insertionTime = measureTime {
            insertionSort(original.toMutableList())
        }
        println("Insertion Sort: ${insertionTime.toDouble(DurationUnit.MILLISECONDS)} ms")

        val selectionTime = measureTime {
            selectionSort(original.toMutableList())
        }
        println("Selection Sort: ${selectionTime.toDouble(DurationUnit.MILLISECONDS)} ms")

        val mergeTime = measureTime {
            mergeSort(original.toMutableList())
        }
        println("Merge Sort: ${mergeTime.toDouble(DurationUnit.MILLISECONDS)} ms")

        val heapTime = measureTime {
            heapSort(original.toMutableList())
        }
        println("Heap Sort: ${heapTime.toDouble(DurationUnit.MILLISECONDS)} ms")
    }
}
