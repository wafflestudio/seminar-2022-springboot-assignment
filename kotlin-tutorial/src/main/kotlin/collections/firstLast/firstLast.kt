package collections.firstLast

// first, last
val numbers = listOf(1, -2, 3, -4, 5, -6)

val first = numbers.first()
val last = numbers.last()

val firstEven = numbers.first { it % 2 == 0 } // 조건을 만족하는(=짝수인) 가장 첫 원소
val lastOdd = numbers.last { it % 2 != 0 } // 조건을 만족하는(=홀수인) 가장 끝 원소

// firstOrNull, lastOrNull
val words = listOf("foo", "bar", "baz", "faz")
val empty = emptyList<String>()

val firstWord = empty.firstOrNull() // 리스트가 비어있으므로 null
val lastWord = empty.lastOrNull() // 리스트가 비어있으므로 null

val firstF = words.firstOrNull { it.startsWith('f') } // f로 시작하는 가장 첫 원소
val firstZ = words.firstOrNull { it.startsWith('z') } // z로 시작하는 가장 첫 원소인데 없으므로 null
val lastF = words.lastOrNull { it.endsWith('f') } // f로 끝나는 가장 끝 원소인데 없으므로 null
val lastZ = words.lastOrNull { it.endsWith('z') } // z로 끝나는 가장 끝 원소

fun main() {
    println(numbers)
    println(first)
    println(last)
    println(firstEven)
    println(lastOdd)

    println()

    println(words)
    println(firstWord)
    println(lastWord)
    println(firstF)
    println(firstZ)
    println(lastF)
    println(lastZ)
}