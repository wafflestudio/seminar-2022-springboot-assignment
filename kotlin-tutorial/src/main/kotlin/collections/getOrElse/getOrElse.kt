package collections.getOrElse

val list = listOf(0, 10, 20)
val map = mutableMapOf<String, Int?>()

fun main() {
    println(list.getOrElse(1) { 42 })
    println(list.getOrElse(10) { 42 }) // 10은 list의 index 범위를 벗어나므로 42 반환

    println()

    println(map.getOrElse("x") { 1 }) // "x"가 map의 key가 아니므로 1 반환

    map["x"] = 3
    println(map.getOrElse("x") { 1 }) // "x"가 map의 key이므로 value 3 반환

    map["x"] = null
    println(map.getOrElse("x") { 1 }) // "x"가 map의 key이지만 value가 null이므로 1 반환
}