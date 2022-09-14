package collections.count

val numbers = listOf(1, -2, 3, -4, 5, -6)

val totalCount = numbers.count() // 전체 개수
val evenCount = numbers.count { it % 2 == 0 } // 조건을 만족하는(=짝수인) 개수

fun main() {
    println(numbers)
    println(totalCount)
    println(evenCount)
}