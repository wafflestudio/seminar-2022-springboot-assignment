package scopeFunctions.apply

data class Person(var name: String, var age: Int, var about: String) {
    constructor() : this("", 0, "") // 직접 만든 생성자
}

fun main() {
    val jake = Person() // name = "", age = 0, about = ""
    println(jake)

    val stringDescription = jake.apply { // 인스턴스 jake에 아래 코드를 실행
        name = "Jake" // jake.name = "Jake"와 동일(apply 안에 있기 때문)
        age = 30
        about = "Android developer"
    }.toString() // apply는 jake를 반환. 따라서 .toString()을 이어서 사용 가능
    println(stringDescription)
}