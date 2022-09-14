package functional.higherOrderFunctions

// Taking Functions as Parameters
// 함수를 다른 함수의 인자로 받을 수 있다
// 인자로 들어온 함수의 파라미터들 타입/리턴값 타입을 결정해두었음
fun calculate(x: Int, y: Int, operation: (Int, Int) -> Int): Int {
    return operation(x, y)
}

fun sum(x: Int, y: Int) = x + y

// Returning Functions
// 함수를 다른 함수의 리턴값으로 쓸 수 있다
fun operation(): (Int) -> Int {
    return ::square
}

fun square(x: Int) = x * x

fun main() {
    val sumResult = calculate(4, 5, ::sum) // ::sum은 sum이라는 이름의 함수를 가리킴
    val mulResult = calculate(4, 5) {a, b -> a * b} // 함수를 람다식 꼴로 넣어줌
    println("sumResult $sumResult, mulResult $mulResult")

    val func = operation() // func = square()
    println(func(2)) // square(2) = 4가 출력됨
}

