interface Stack<T> {
    fun push(data: T)
    fun pop(): T?
    fun peek(): T?
    fun isEmpty(): Boolean
}

class LinkedListStack<T> : Stack<T> {
    private val list = DoublyLinkedList<T>()

    override fun push(data: T) = list.pushFront(data)
    override fun pop(): T? = list.popFront()
    override fun peek(): T? = list.peekFront()
    override fun isEmpty(): Boolean = list.isEmpty()
}
