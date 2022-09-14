package controlFlow.loops

val cakes = listOf("carrot", "cheese", "chocolate")

fun eatACake() = println("Eat a Cake")
fun bakeACake() = println("Bake a Cake")

class Animal(val name: String)
class Zoo(val animals: List<Animal>) {
    operator fun iterator(): Iterator<Animal> {
        return animals.iterator()
    }
}

fun main() {
    // for
    for (cake in cakes) {
        println("Yummy, it's a $cake cake!")
    }

    println()

    // while and do-while
    var cakesEaten = 0
    var cakesBaked = 0
    while (cakesEaten < 5) {
        eatACake()
        cakesEaten++
    }
    do {
        bakeACake()
        cakesBaked++
    } while (cakesBaked < cakesEaten)

    println()

    // Iterators
    val zoo = Zoo(listOf(Animal("Zebra"), Animal("lion")))
    for (animal in zoo) {
        println("Watch out, it's a ${animal.name}")
    }
}