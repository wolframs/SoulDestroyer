package souldestroyer.shared

import java.util.LinkedList

class LimitedQueue<T>(private val maxSize: Int) {
    private val list = LinkedList<T>()

    fun add(value: T) {
        if (list.size >= maxSize) {
            list.removeFirst() // Remove the oldest element
        }
        list.addLast(value) // Add the new element to the end
    }

    fun toList(): List<T> {
        return list.toList()
    }

    fun size(): Int {
        return list.size
    }

    fun isEmpty(): Boolean {
        return list.isEmpty()
    }

    fun clear() {
        list.clear()
    }
}