package scopeFunctions.run

fun getNullableLength(ns: String?) {
    println("for \"$ns\":")
    ns?.run { // 기본적으로는 let과 동일하지만, it이 아닌 this로 접근
        println("\tis empty? " + isEmpty())
        println("\tlength = $length")
        length
    }
}

fun main() {
    getNullableLength(null)
    getNullableLength("")
    getNullableLength("some string with Kotlin")
}