package controlFlow.equalityChecks

fun main() {
    val authors = setOf("Shakespeare", "Hemingway", "Twain")
    val writers = setOf("Twain", "Shakespeare", "Hemingway")

    println(authors == writers) // authors.equals(writers)와 동일, set이므로 순서 무시
    println(authors === writers) // 주소값 비교(java의 ==)
}