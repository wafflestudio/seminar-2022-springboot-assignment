package scopeFunctions.with

class Configuration(var host: String, var port: Int)

fun main() {
    val configuration = Configuration(host = "127.0.0.1", port = 9000)

    with(configuration) { // 인스턴스 이름(configuration)을 with 내부에서는 생략 가능
        println("$host:$port")
    }

    // instead of:
    println("${configuration.host}:${configuration.port}")
}