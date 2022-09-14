package productivityBoosters

import java.time.LocalDate
import java.time.chrono.ChronoLocalDate

fun format(userName: String, domain: String) = "$userName@$domain"

fun main() {
    // Named Arguments
    println(format("mario", "example.com"))
    println(format("domain.com", "username"))
    println(format(userName = "foo", domain = "bar.com"))
    println(format(domain = "frog.com", userName = "pepe")) // 순서 바뀌어도 이름을 지정해주었으므로 ok

    println()

    // String Templates
    val greeting = "Kotliner"
    println("Hello $greeting") // $greeting : greeting.toString()이 들어감
    println("Hello ${greeting.uppercase()}") // 비슷하지만 expression이 들어가므로 중괄호를 쳐줌.

    println()

    // Destructuring Declarations
    val (x, y, z) = arrayOf(5, 10, 15) // x = 5, y = 10, z = 15
    val map = mapOf("Alice" to 21, "Bob" to 25)
    for ((name, age) in map) {
        println("$name is $age years old")
    }
    val (min, max) = findMinMax(listOf(100, 90, 50, 98, 76, 83)) // min = 50, max = 100

    val user = getUser()
    val (username, email) = user
    println(username == user.component1()) // true. component1이 첫번째인 username에 대응
    val (_, emailAddress) = getUser() // emailAddress는 두번째 원소인 email에 대응됨. 첫번째 원소를 스킵하기 위해 _ 사용
    println(emailAddress) // mary@somewhere.com 출력

    val (num, name) = Pair(1, "one") // 직접 component1(), component2()를 만든 경우
    println("num = $num, name = $name")

    println()

    // Smart Casts
    val date: ChronoLocalDate? = LocalDate.now()
    if (date != null) {
        // non-nullable로 smart-cast 됨
        println(date.isLeapYear) // 올해(2022년)는 LeapYear(윤년)이 아니므로 false 출력
    }
    // 조건문 안쪽에서 non-nullable로 smart-cast 됨
    if (date != null && date.isLeapYear) {
        println("It's a leap year!")
    }
    // 조건문 안쪽에서 non-nullable로 smart-cast 됨
    if (date == null || !date.isLeapYear) {
        println("There's no Feb 29 this year...")
    }

    if (date is LocalDate) {
        val month = date.monthValue // ChronoLocalDate 타입이 LocalDate로 smart-cast 됨
        println(month)
    }
}

fun findMinMax(list: List<Int>): Pair<Int, Int> {
    // do the math
    return Pair(50, 100)
}

data class User(val username: String, val email: String)

fun getUser() = User("Mary", "mary@somewhere.com")

class Pair<K, V>(val first: K, val second: V) {
    operator fun component1(): K {
        return first
    }

    operator fun component2(): V {
        return second
    }
}