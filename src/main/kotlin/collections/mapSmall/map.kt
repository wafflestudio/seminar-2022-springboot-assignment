package collections.mapSmall

val numbers = listOf(1, -2, 3, -4, 5, -6)

val doubled = numbers.map { x -> x * 2 } // 각 원소를 람다식의 리턴값으로 바꿔줌(= 모든 원소 두배)

val tripled = numbers.map { it * 3 } // 각 원소를 람다식의 리턴값으로 바꿔줌(= 모든 원소 세배)

fun main() {
    println(numbers)
    println(doubled)
    println(tripled)
}