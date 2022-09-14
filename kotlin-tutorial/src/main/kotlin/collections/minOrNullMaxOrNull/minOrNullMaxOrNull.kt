package collections.minOrNullMaxOrNull

val numbers = listOf(1, 2, 3)
val empty = emptyList<Int>()
val only = listOf(3)

fun main() {
    println("Numbers: $numbers, min = ${numbers.minOrNull()} max = ${numbers.maxOrNull()}")
    println("Empty: $empty, min = ${empty.minOrNull()} max = ${empty.maxOrNull()}")
    println("Only: $only, min = ${only.minOrNull()} max = ${only.maxOrNull()}")
}