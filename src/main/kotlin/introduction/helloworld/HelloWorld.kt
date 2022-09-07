package introduction.helloworld

fun main() {
    println("Hello, World!")
}


// 코틀린 1.3 이전 : main 함수는 반드시 Array<String> 타입의 parameter를 필요로 함
fun main(args: Array<String>) {
    println("Hello, World!")
}