package collections.filter

val numbers = listOf(1, -2, 3, -4, 5, -6)

val positives = numbers.filter { x -> x > 0 } // 람다식의 값이 true인, 즉 양수만 반환

val negatives = numbers.filter { it < 0 } // 람다식의 값이 true인, 즉 음수만 반환

fun main() {
    println(numbers)
    println(positives)
    println(negatives)
}