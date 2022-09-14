package collections.partition

val numbers = listOf(1, -2, 3, -4, 5, -6)

fun main() {
    val evenOdd = numbers.partition { it % 2 == 0 } // (true인 원소들의 list, false인 원소들의 list) pair
    // positives = true인 원소들의 list
    // negatives = false인 원소들의 list
    val (positives, negatives) = numbers.partition { it > 0 }

    println(numbers)
    println(evenOdd)
    println(positives)
    println(negatives)
}