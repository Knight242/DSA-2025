fun <T> reverseStack(stack: LinkedListStack<T>) {
    val tempQueue = LinkedListQueue<T>()

    // Move all elements from stack to queue
    while (!stack.isEmpty()) {
        tempQueue.enqueue(stack.pop()!!)
    }

    // Now dequeue back into stack
    while (!tempQueue.isEmpty()) {
        stack.push(tempQueue.dequeue()!!)
    }
}
