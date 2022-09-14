package introduction.variables

fun main() {
    var a: String = "Initial" // mutable variable 선언 & 초기화
    println(a)
    val b: Int = 1 // immutable variable 선언 & 초기화
    val c = 3 // immutable variable 선언 & 초기화, 컴파일러가 타입을 Int로 추론

    var e: Int // mutable variable 선언만 함
    // println(e) : 컴파일 에러 : 초기화되지 않은 변수

    val d: Int // mutable variable 선언만 함
    if (a == "Initial") { // 반드시 어딘가에서 초기화가 되므로 ok
        d = 1
    } else {
        d = 2
    }

    println(d) // 호출 전에 초기화가 되었으므로 문제 없음
}