package specialClasses.dataClasses

// data class : data를 다루기 쉽게 만들어줌
// copy(), hashCode() 등을 알아서 지원해줌
data class User(val name: String, val id: Int) {
    override fun equals(other: Any?) =
        other is User && other.id == this.id
}

fun main() {
    val user = User("Alex", 1)
    println(user)

    val secondUser = User("Alex", 1)
    val thirdUser = User("Max", 2)
    val forthUser = User("Max", 1)

    println("user == secondUser: ${user == secondUser}")
    println("user == thirdUser: ${user == thirdUser}")
    println("user == forthUser: ${user == forthUser}") // equals가 id만 비교하기 때문

    // hashCode() function
    println(user.hashCode())
    println(secondUser.hashCode())
    println(thirdUser.hashCode())
    println(forthUser.hashCode()) // user과 다름. hashCode는 name, id를 둘 다 보기 때문

    // copy() function
    println(user.copy())
    println(user === user.copy())
    println(user.copy("Max"))
    println(user.copy(id = 3))

    println("name = ${user.component1()}") // 첫번째 변수인 name을 가져옴
    println("id = ${user.component2()}") // 두번째 변수인 id를 가져옴
}