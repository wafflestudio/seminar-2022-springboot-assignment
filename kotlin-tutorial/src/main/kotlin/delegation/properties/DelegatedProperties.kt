package delegation.properties

import kotlin.reflect.KProperty

class Example {
    var p: String by Delegate() // String 타입의 프로퍼티 p의 get/set 메서드 호출을 Delegate 클래스의 인스턴스에 위임함 

    override fun toString() = "Example Class"
}

class Delegate() {
    // getValue 연산자를 오버로딩함
    // thisRef : 선언된 위치(여기서는 Example 클래스의 인스턴스 e)
    // prop.name : 소스 코드에서 선언된 이름(KCallable에 정의됨, KProperty는 KCallable의 하위 인터페이스)
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): String { // get 메서드가 위임된 곳
        return "$thisRef, thank you for delegating '${prop.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: String) { // set 메서드가 위임된 곳
        println("$value has been assigned to ${prop.name} in $thisRef")
    }
}

// Standard Delegates
class LazySample {
    init {
        println("created!")
    }

    val lazyStr: String by lazy { // lazy initialization에 사용
        println("computed!")
        "my lazy" // lazyStr에 들어간 값
    }
}

// Storing Properties in a Map
class User(val map: Map<String, Any?>) {
    val name: String by map // map 안의 getValue("name")이 실행됨 -> name = "John Doe"
    val age: Int by map // map 안의 getValue("age")가 실행됨 -> age = 25
}

fun main() {
    val e = Example()
    println(e.p) // thisRef = e.toString() = "Example Class", prop.name = 'p'
    e.p = "NEW" // // thisRef = e.toString() = "Example Class", prop.name = 'p', value = "NEW"

    println()

    val sample = LazySample() // 클래스 초기화때 init() 호출 -> created! 출력
    println("lazyStr = ${sample.lazyStr}") // lazyStr을 처음으로 가져옴 -> lazy 안의 람다식 실행
    println(" = ${sample.lazyStr}") // lazyStr을 두번째로 가져옴 -> 이미 들어있는 값 "my lazy" 반환

    println()

    val user = User(
        mapOf( // Map [("name", "John Doe"), ("age", 25)]가 파라미터가 됨
            "name" to "John Doe",
            "age" to 25
        )
    )

    println("name = ${user.name}, age = ${user.age}")
}