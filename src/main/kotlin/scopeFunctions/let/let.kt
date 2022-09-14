package scopeFunctions.let

fun customPrint(s: String) {
    print(s.uppercase())
}

fun main() {
    val empty = "test".let { // let 블록 안에 있는 코드들 실행
        customPrint(it)
        it.isEmpty() // let이 반환하는 값 : false
    }
    println(" is empty: $empty")

    fun printNonNull(str: String?) {
        println("Printing \"$str\":")

        str?.let { // ?.let 이므로 null이면 let 블록 안의 코드는 실행되지 않는다
            print("\t")
            customPrint(it)
            println()
        }
    }

    fun printIfBothNonNull(strOne: String?, strTwo: String?) {
        strOne?.let { firstString ->
            strTwo?.let { secondString ->
                customPrint("$firstString : $secondString")
                println()
            }
        }
    }

    printNonNull(null)
    printNonNull("my string")
    printIfBothNonNull("First", "Second")
}