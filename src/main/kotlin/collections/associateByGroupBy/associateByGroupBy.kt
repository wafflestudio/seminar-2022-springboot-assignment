package collections.associateByGroupBy

data class Person(val name: String, val city: String, val phone: String)

val people = listOf(
    Person("John", "Boston", "+1-888-123456"),
    Person("Sarah", "Munich", "+49-777-789123"),
    Person("Svyatoslav", "Saint-Petersburg", "+7-999-456789"),
    Person("Vasilisa", "Saint-Petersburg", "+7-999-123456")
)

val phoneBook = people.associateBy { it.phone } // key : it.phone, value : it을 map에 추가
val cityBook = people.associateBy(Person::phone, Person::city) // key : it.phone, value : it.city를 map에 추가
val peopleCities = people.groupBy(Person::city, Person::name) // key : it.city, value : 해당 key에 해당하는 it.name들의 리스트
val lastPersonCity = people.associateBy(Person::city, Person::name) // 중복 키가 들어오면 마지막에 들어온 것이 남는다

fun main() {
    println(people)
    println(phoneBook)
    println(cityBook)
    println(peopleCities)
    println(lastPersonCity)
}