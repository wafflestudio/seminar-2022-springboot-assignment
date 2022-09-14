package functional.lambdaFunctions

val upperCase1: (String) -> String = { str: String -> str.uppercase()}
val upperCase2: (String) -> String = { str -> str.uppercase()} // 타입을 알아서 추론해줌
val upperCase3 = { str: String -> str.uppercase()}
// val upperCase4 = { str -> str.uppercase() } // 타입 추론 불가능! 파라미터의 타입을 모르기 때문
val upperCase5: (String) -> String = { it.uppercase() } // 람다식의 파라미터가 하나라면 it으로 대체 가능
val upperCase6: (String) -> String = String::uppercase // 함수포인터 :: 사용해도 됨

fun main() {
    println(upperCase1("hello"))
    println(upperCase2("hello"))
    println(upperCase3("hello"))
    println(upperCase5("hello"))
    println(upperCase6("hello"))
}
