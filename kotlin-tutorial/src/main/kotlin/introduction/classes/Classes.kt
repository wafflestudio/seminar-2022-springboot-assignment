package introduction.classes

class Customer // 아무것도 없는 클래스 생성

class Contact(val id: Int, var email: String) // 상수 id, 변수 email로 구성. 생성자는 알아서 추가됨.

fun main() {
    val customer = Customer()
    val contact = Contact(1, "mary@gmail.com")

    println(contact.id)
    contact.email = "jain@gmail.com"
    println(contact.email)
}