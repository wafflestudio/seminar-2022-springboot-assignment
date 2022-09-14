package collections.flatMap

val fruitsBag = listOf("apple", "orange", "banana", "grapes")
val clothesBag = listOf("shirts", "pants", "jeans")
val cart = listOf(fruitsBag, clothesBag) // list의 list
val mapBag = cart.map { it } // list의 list(map 자기자신 : cart와 사실 동등함)
val flatMapBag = cart.flatMap { it } // list의 각 원소를 풀어서 하나의 list로 만듦.

fun main() {
    println(fruitsBag)
    println(clothesBag)
    println(cart) // [[apple, orange, banana, grapes], [shirts, pants, jeans]]
    println(mapBag) // [[apple, orange, banana, grapes], [shirts, pants, jeans]]
    println(flatMapBag) // [apple, orange, banana, grapes, shirts, pants, jeans]
}