package introduction.functions

// Unit은 java의 void에 해당함
fun printMessage(message: String): Unit {
    println(message)
}

// prefix에 default value로 "Info"를 줌
fun printMessageWithPrefix(message: String, prefix: String = "Info") {
    println("[$prefix] $message")
}

// 변수(or 함수) 이름: 변수(or 함수의 리턴) 타입과 같은 방식으로 작성
fun sum(x: Int, y: Int): Int {
    return x + y
}

// 간단하게 아래처럼 쓸 수도 있다
fun multiply(x: Int, y: Int) = x * y

fun main() {
    // Default Parameter Values and Named Arguments
    printMessage("Hello")
    printMessageWithPrefix("Hello", "Log")
    printMessageWithPrefix("Hello")
    // parameter 이름을 명시해주면 순서를 바꿀 수도 있다(named arguments)
    printMessageWithPrefix(prefix = "Log", message = "Hello")
    println(sum(1, 2))
    println(multiply(2, 4))

    // Infix Functions
    // infix fun typeA.xxx(B: typeB) 호출 방법 : A xxx B
    infix fun Int.time(str: String) = str.repeat(this)
    println(2 time "Bye ")

    val pair = "Ferrari" to "Katrina"
    println(pair)

    infix fun String.onto(other: String) = Pair(this, other)
    val myPair = "McLaren" onto "Lucas"
    println(myPair)

    val sophia = Person("Sophia")
    val claudia = Person("Claudia")
    sophia likes claudia

    println(sophia.likedPeople) // claudia가 들어가 있음
    println(claudia.likedPeople) // 아무것도 없음

    // Operator Functions
    operator fun Int.times(str: String) = str.repeat(this)
    println(2 * "Bye ")

    operator fun String.get(range: IntRange) = substring(range) // bracket-access syntax
    val str = "Always forgive your enemies; nothing annoys them so much."
    println(str[0..14]) // 0부터 14까지(14를 포함함)

    // Functions with vararg Parameters
    fun printAll(vararg messages: String) {
        for (m in messages) println(m)
    }
    printAll("Hello", "Hallo", "Salut", "Hola", "你好")

    fun printAllWithPrefix(vararg messages: String, prefix: String) {
        for (m in messages) println(prefix + m)
    }
    // prefix = 을 빼면 컴파일 에러. vararg가 어디까지인지 추론이 불가능하기 때문
    printAllWithPrefix(
        "Hello", "Hallo", "Salut", "Hola", "你好",
        prefix = "Greeting: "
    )
    // *을 빼면 컴파일 에러. entries는 Array<String>으로 취급되기 때문.
    fun log(vararg entries: String) {
        printAll(*entries)
    }
    log("Hello", "Hallo", "Salut", "Hola", "你好")
}

class Person(val name: String) {
    val likedPeople = mutableListOf<Person>()
    infix fun likes(other: Person) {
        likedPeople.add(other)
    }
}