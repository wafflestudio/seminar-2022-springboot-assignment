package com.wafflestudio.seminar.test

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
fun main() {
    // 여기를 채워 주세요!
    val initialInput = readLine()!!
    val processedData = initialInput.substring(1, initialInput.length-1).split(',').map{ c -> c.replace("\"", "")}
    val excel = Table(processedData)
    while(true){
        val commandInput = readLine()!!.split(" ")
        when(commandInput[0]){
            "move" -> excel.move(commandInput[1], commandInput[2].toInt())
            "delete" -> excel.delete()
            "restore" -> excel.restore()
            "list" -> excel.list()
            "q" -> break
            else -> println("ERROR")
        }
    }
}
