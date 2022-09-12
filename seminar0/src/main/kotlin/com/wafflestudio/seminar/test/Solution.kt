package com.wafflestudio.seminar.test

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
fun move(now:Int, way:String, num:Int):Int {
    if (way == "-u") {
        return now - num
    } else {
        return now + num
    }
}

class Student(var id:Int, var name:String) {

}

fun main() {
    var dt = readLine()!!.split("\"")
    var students = mutableListOf<String>()
    for (x in 0..(dt.size-1)) {
        if (dt[x] in arrayOf(",", "[", "]")) {
            continue
        } else {
            students.add(dt[x])
        }
    }
    var restoration = mutableListOf<Student>()
    var now = 0

    while (true) {
        var order = readLine()!!.split(" ")
        if (order[0] == "q") {
            break
        }
        if (order[0] == "move") {
            var next = move(now, order[1], order[2].toInt())
            if (next < 0 || next >= students.size) {
                println("Error 100")
            } else {
                now = next
            }
        } else if (order[0] == "delete") {
            var studentName = students.removeAt(now)
            var student = Student(now, studentName)
            restoration.add(student)
            if (now == students.size) {
                now--
            }
        } else if (order[0] == "restore") {
            if (restoration.size == 0) {
                println("Error 200")
            } else {
                var restore = restoration.removeLast()
                students.add(restore.id, restore.name)
                if (restore.id <= now) {
                    now++
                }
            }
        } else if (order[0] == "list") {
            students.forEach {
                println(it)
            }
        }
    }
}