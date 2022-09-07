package introduction.generics

// Generic Classes
class MutableStack<E>(vararg items: E) {
    private val elements = items.toMutableList()
    fun push(element: E) = elements.add(element)
    fun peek(): E = elements.last()
    fun pop(): E = elements.removeAt(elements.size - 1)
    fun isEmpty() = elements.isEmpty()
    fun size() = elements.size
    override fun toString() = "MutableStack(${elements.joinToString()})"
}

fun main() {
    val stack = MutableStack(0.62, 3.14, 2.7, 9.87)
    println(stack)
    println("peek(): " + stack.peek())
    println(stack)
    println("pop(): " + stack.pop())
    println(stack)
    println("pop(): " + stack.pop())
    println(stack)
    println("pop(): " + stack.pop())
    println(stack)
    println("pop(): " + stack.pop())
    println(stack)
    println("isEmpty(): " + stack.isEmpty())
    println()

    // Generic Functions
    fun <E> mutableStackOf(vararg elements: E) = MutableStack(*elements)
    val stack2 = mutableStackOf(0.62, 3.14, 2.7)
    println(stack2)
}