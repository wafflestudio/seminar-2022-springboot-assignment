package specialClasses.enumClasses

enum class State {
    IDLE, RUNNING, FINISHED
}

enum class Color(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF),
    YELLOW(0xFFFF00);

    fun containsRed() = (this.rgb and 0xFF0000 != 0) // and : bitwise & 연산
}


fun main() {
    val state = State.RUNNING
    val message = when (state) {
        State.IDLE -> "It's idle"
        State.RUNNING -> "It's running"
        State.FINISHED -> "It's finished"
    }
    println(message)

    println()

    val red = Color.RED
    println(red)
    println(red.containsRed())
    println(Color.GREEN.containsRed())
    println(Color.BLUE.containsRed())
    println(Color.YELLOW.containsRed())
}