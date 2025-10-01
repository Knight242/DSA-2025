interface LinkedList<T> {
    fun pushFront(data: T)
    fun pushBack(data: T)
    fun popFront(): T?
    fun popBack(): T?
    fun peekFront(): T?
    fun peekBack(): T?
    fun isEmpty(): Boolean
}

class DoublyLinkedList<T> : LinkedList<T> {


    private class Node<T>(val data: T) {
        var prev: Node<T>? = null
        var next: Node<T>? = null
    }

    private var head: Node<T>? = null  // First node
    private var tail: Node<T>? = null  // Last node


    override fun pushFront(data: T) {
        val newNode = Node(data)
        if (head == null) {
            head = newNode
            tail = newNode
        } else {
            newNode.next = head
            head!!.prev = newNode
            head = newNode
        }
    }


    override fun pushBack(data: T) {
        val newNode = Node(data)
        if (tail == null) {
            head = newNode
            tail = newNode
        } else {
            tail!!.next = newNode
            newNode.prev = tail
            tail = newNode
        }
    }


    override fun popFront(): T? {
        if (head == null) return null
        val value = head!!.data
        head = head!!.next
        if (head != null) {
            head!!.prev = null
        } else {
            tail = null
        }
        return value
    }

    override fun popBack(): T? {
        if (tail == null) return null
        val value = tail!!.data
        tail = tail!!.prev
        if (tail != null) {
            tail!!.next = null
        } else {
            head = null
        }
        return value
    }


    override fun peekFront(): T? {
        return head?.data
    }


    override fun peekBack(): T? {
        return tail?.data
    }


    override fun isEmpty(): Boolean {
        return head == null
    }

}

fun main() {
    val list = DoublyLinkedList<Int>()

    list.pushFront(10)     // List: 10
    list.pushFront(20)     // List: 20 -> 10
    list.pushBack(5)       // List: 20 -> 10 -> 5

    println(list.popFront()) // Removes 20
    println(list.popBack())  // Removes 5

    println("Front: ${list.peekFront()}") // Front: 10
    println("Empty? ${list.isEmpty()}")
}
