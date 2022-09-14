package collections.zip

val A = listOf("a", "b", "c")
val B = listOf(1, 2, 3, 4)

val resultPairs = A zip B // A의 원소와 B의 원소를 순서대로 pair로 묶고, pair들의 list를 반환. 짝이 안 맞는 원소들은 버려짐
val resultReduce = A.zip(B) { a, b -> "$a$b" } // pair대신 람다식을 수행한 결과들의 list를 반환.

fun main() {
    println("A to B: $resultPairs") // [(a, 1), (b, 2), (c, 3)]
    println("\$A\$B: $resultReduce") // [a1, b2, c3]
}