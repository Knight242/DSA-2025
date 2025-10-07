/**
 * Minimum priority queue interface that defines basic operations
 * Where the lower the priority value,
 * the sooner an element is removed
 *
 * @param T the type of elements stored in the queue
 */
interface MinPriorityQueue<T> {
    /**
     * Checks whether the queue is empty.
     * @return "true" if the queue has no elements, else "false"
     */
    fun isEmpty(): Boolean
    /**
     * Inserts [elem] into the queue with the given [priority]
     * Lower [priority] values are "more urgent"
     *
     * @param elem the element to insert
     * @param priority the priority value (lower values removed first)
     */
    fun addWithPriority(elem: T, priority: Double)
    /**
     * Removes and returns the element with the "lowest" priority
     *
     * @return the element with the highest priority or `null` if empty
     */
    fun next(): T?
    /**
     * Adjusts the priority of an existing [elem]
     * If the element is not present, there is no adjustPriority()
     *
     * @param elem the element whose priority to change
     * @param newPriority the new priority value
     */
    fun adjustPriority(elem: T, newPriority: Double)
}

/**
 * A node in the heap containing an element and its corresponding [priority]
 * @param T the type of the element stored
 * @property element the data stored in the heap
 * @property priority the priority value for ordering
 */
data class HeapNode<T>(
    val element: T,
    var priority: Double
)

/**
 * A "min-heap" used by [PriorityQueue]
 * to keep track of elements ordered by priority
 * @param T the type of elements stored
 */
class MinHeap<T> {

    /** A Mutable list that stores the heap in array-based binary-tree form. */
    private val heap = mutableListOf<HeapNode<T>>()

    /** @return `true` if the heap is empty. */
    fun isEmpty(): Boolean = heap.isEmpty()

    /** @return the number of elements currently in the heap. */
    fun size(): Int = heap.size

    /**
     * Inserts a [node] into the heap and restores the "min-heap"
     * @param node the [HeapNode] to insert
     */
    fun insert(node: HeapNode<T>) {
        heap.add(node)
        siftUp(heap.lastIndex)
    }
    /**
     * Removes and returns the [HeapNode] with the "lowest" priority value.
     * @return the node with the lowest priority, or `null` if heap is empty
     */
    fun extractMin(): HeapNode<T>? {
        if (heap.isEmpty()) return null
        val min = heap[0]
        val last = heap.removeAt(heap.lastIndex)
        if (heap.isNotEmpty()) {
            heap[0] = last
            siftDown(0)
        }
        return min
    }

    /**
     * Updates the priority of [elem] to [newPriority]
     * Moves the element up or down when needed
     * If the element doesn't exist, updatePriority() doesn't happen
     * @param elem the element whose priority should change
     * @param newPriority the new priority value
     */
    fun updatePriority(elem: T, newPriority: Double) {
        val index = heap.indexOfFirst { it.element == elem }
        if (index == -1) return
        val oldPriority = heap[index].priority
        heap[index].priority = newPriority
        if (newPriority < oldPriority) {
            siftUp(index)
        } else {
            siftDown(index)
        }
    }

    /**
     * Moves the node at [index] upward until the min-heap property is restored
     */
    private fun siftUp(index: Int) {
        var i = index
        while (i > 0) {
            val parent = (i - 1) / 2
            if (heap[i].priority < heap[parent].priority) {
                heap.swap(i, parent)
                i = parent
            } else break
        }
    }

    /**
     * Moves the node at [index] downward until the min-heap property is restored
     */
    private fun siftDown(index: Int) {
        var i = index
        val size = heap.size
        while (true) {
            val left = 2 * i + 1
            val right = 2 * i + 2
            var smallest = i

            if (left < size && heap[left].priority < heap[smallest].priority) smallest = left
            if (right < size && heap[right].priority < heap[smallest].priority) smallest = right

            if (smallest != i) {
                heap.swap(i, smallest)
                i = smallest
            } else break
        }
    }

    /**
     * Swaps two elements in the [heap] at indices [i] and [j].
     */
    private fun MutableList<HeapNode<T>>.swap(i: Int, j: Int) {
        val tmp = this[i]
        this[i] = this[j]
        this[j] = tmp
    }
}

/**
 * Implementation of [MinPriorityQueue] that uses a [MinHeap]
// * to get the element with the lowest priority.
 *
 * @param T the type of elements stored
 */
class PriorityQueue<T> : MinPriorityQueue<T> {

    /** The heap that holds the queue  */
    private val minHeap = MinHeap<T>()

    override fun isEmpty(): Boolean = minHeap.isEmpty()

    override fun addWithPriority(elem: T, priority: Double) {
        minHeap.insert(HeapNode(elem, priority))
    }

    override fun next(): T? = minHeap.extractMin()?.element

    override fun adjustPriority(elem: T, newPriority: Double) {
        minHeap.updatePriority(elem, newPriority)
    }
}
