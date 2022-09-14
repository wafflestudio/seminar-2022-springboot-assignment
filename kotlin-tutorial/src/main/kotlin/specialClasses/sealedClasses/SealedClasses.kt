package specialClasses.sealedClasses

// sealed class는 java의 추상클래스에 대응되는 개념
// sealed class의 자식 클래스는 반드시 같은 패키지에 있어야함
sealed class Mammal(val name: String)

class Cat(val catName: String) : Mammal(catName)
class Human(val humanName: String, val job: String) : Mammal(humanName)

fun greetMammal(mammal: Mammal): String {
    when (mammal) { // else가 필요하지 않다! 컴파일러가 Cat, Human 외의 다른 자식 클래스가 없음을 알기 때문
        is Human -> return "Hello ${mammal.name}; You're working as a ${mammal.job}"
        is Cat -> return "Hello ${mammal.name}"
    }
}

fun main() {
    println(greetMammal(Cat("Snowy")))
}