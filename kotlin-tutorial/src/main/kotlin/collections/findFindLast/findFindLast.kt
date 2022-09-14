package collections.findFindLast

val words = listOf("Lets", "find", "something", "in", "collection", "somehow")

val first = words.find { it.startsWith("some") } // some으로 시작하는 가장 첫 원소 something
val last = words.findLast { it.startsWith("some") } // some으로 시작하는 끝 원소 somehow
val nothing = words.find { it.contains("nothing") } // 없으므로 null

fun main() {
    println(words)
    println(first)
    println(last)
    println(nothing)
}