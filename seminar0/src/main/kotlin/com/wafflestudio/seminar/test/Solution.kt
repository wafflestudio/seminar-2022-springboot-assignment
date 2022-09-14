package com.wafflestudio.seminar.test

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
fun main() {
    val studentList = StudentList(
        toStudentList(readLine()!!)
    )
    var str = readLine()
    while (str!!.trim() != "q") {
        studentList.command(toCommand(str))
        str = readLine()
    }
}