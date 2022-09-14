package introduction.inheritance

// open이 붙어있는 클래스만 부모 클래스가 될 수 있다(기본이 java의 final)
open class Dog {
    open fun sayHello() { // open이 붙어있는 함수만 오버라이드가 가능하다(기본이 java의 final)
        println("wow wow!")
    }
}

class Yorkshire : Dog() { // Dog를 상속받은 클래스
    override fun sayHello() {
        println("wif wif!")
    }
}

// Inheritance with Parameterized Constructor
open class Tiger(val origin: String) {
    fun sayHello() {
        println("A tiger from $origin says: grrhhh!")
    }
}

class SiberianTiger : Tiger("Siberia")

// Passing Constructor Arguments to Superclass
open class Lion(val name: String, val origin: String) {
    fun sayHello() {
        println("$name, the lion from $origin says: groah!")
    }
}

// 선언부의 name에 var, val이 붙지 않음. Lion의 name을 이어받기 때문
class Asiatic(name: String) : Lion(name = name, origin = "India")

fun main() {
    val dog: Dog = Yorkshire()
    dog.sayHello()

    val tiger: Tiger = SiberianTiger()
    tiger.sayHello()

    val lion: Lion = Asiatic("Rufo")
    lion.sayHello()
}