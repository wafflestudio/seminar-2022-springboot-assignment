package collections.anyAllNone

// any : 람다식이 true인 원소가 하나라도 있으면 true, 아니면 false
val numbers = listOf(1, -2, 3, -4, 5, -6)
val anyNegative = numbers.any { it < 0 }
val anyGT6 = numbers.any { it > 6 }

// all : 모든 원소가 람다식이 true가 되면 true, 아니면 false
val allEven_all = numbers.all { it % 2 == 0 }
val allLess6_all = numbers.all { it < 6 }

// none : 람다식이 true인 원소가 없으면 true, 아니면 false(any와 정확히 반대임)
val allEven_none = numbers.none { it % 2 == 1 }
val allLess6_none = numbers.none { it > 6 }

fun main() {
    println(numbers)
    println(anyNegative)
    println(anyGT6)
    println()
    println(allEven_all)
    println(allLess6_all)
    println()
    println(allEven_none)
    println(allLess6_none)
}