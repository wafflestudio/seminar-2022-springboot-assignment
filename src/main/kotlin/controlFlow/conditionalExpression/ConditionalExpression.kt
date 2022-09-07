package controlFlow.conditionalExpression

// 3항 연산자 대신 if (조건문) 참일때 값 else 거짓일때 값 의 형태로 사용
fun max(a: Int, b: Int) = if (a > b) a else b

fun main() {
    println(max(99, -42))
}