package scopeFunctions.also

data class Person(var name: String, var age: Int, var about: String)

fun writeCreationLog(p: Person) {
    println("A new person ${p.name} was created.")
}

fun main() {
    val jake = Person("Jake", 30, "Android developer")
        .also { // apply와 유사하며, 자기 자신은 it으로 넣어줌.(apply는 this)
            writeCreationLog(it)
        }
    println(jake)
}