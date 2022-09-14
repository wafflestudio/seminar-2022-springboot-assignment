package controlFlow.ranges

fun main() {
    for (i in 0..3) { // 3도 포함
        print(i)
    }
    print(" ")
    for (i in 0 until 3) { // 3 제외
        print(i)
    }
    print(" ")
    for (i in 2..8 step 2) { // 2 간격으로
        print(i)
    }
    print(" ")
    for (i in 3 downTo 0) { // 0 포함, 역방향
        print(i)
    }

    println()

    for (c in 'a'..'d') { // char로도 int와 똑같이 지원됨
        print(c)
    }
    print(" ")
    for (c in 'z' downTo 's' step 2) {
        print(c)
    }
    print(" ")

    println()

    val x = 2
    if (x in 1..5) {
        print("x is in range from 1 to 5")
    }
    println()

    if (x !in 6..10) {
        print("x is not in range from 6 to 10")
    }
}