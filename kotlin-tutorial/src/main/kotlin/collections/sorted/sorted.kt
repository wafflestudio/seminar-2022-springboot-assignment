package collections.sorted

import kotlin.math.abs

val shuffled = listOf(5, 4, 2, 1, 3, -10)
val natural = shuffled.sorted() // 오름차순 정렬
val inverted = shuffled.sortedBy { -it } // -it의 오름차순 정렬(=내림차순 정렬) 
val descending = shuffled.sortedDescending() // 내림차순 정렬
val descendingBy = shuffled.sortedByDescending { abs(it) } // 절댓값의 내림차순 정렬

fun main() {
    println("Shuffled: $shuffled")
    println("Natural order: $natural")
    println("Inverted natural order: $inverted")
    println("Inverted natural order value: $descending")
    println("Inverted natural order of absolute values: $descendingBy")
}