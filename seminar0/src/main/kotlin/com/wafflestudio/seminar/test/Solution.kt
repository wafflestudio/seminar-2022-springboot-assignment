package com.wafflestudio.seminar.test

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */

fun main() {
    // 여기를 채워 주세요!
    var firstString = readln()
    var studentList = stringToList(firstString)
    var listManagementProgram = ListManagementProgram(studentList) 
    var command = readln()
    while (command != "q") {
        listManagementProgram.manage(command)
        command = readln()
    }
}

fun stringToList(string: String): MutableList<String> {
    val names = string.substring(2, string.length - 2).split("\",\"")
    var list = mutableListOf<String>()
    for (name in names) {
        list.add(name)
    }
    return list
}

class ListManagementProgram(var studentlist: MutableList<String>) {
    var index = 0
    var stack: MutableList<Pair<String, Int>> = mutableListOf()
    
    fun manage(command: String) {
        if (command.startsWith("move -u")) {
            var n = command.substring(8).toInt()
            moveUp(n)
        } else if (command.startsWith("move -d")) {
            var n = command.substring(8).toInt()
            moveDown(n)
        } else if (command == "delete") {
            delete()
        } else if (command == "restore") {
            restore()
        } else if (command == "list") {
            list()
        }
    }
    
    fun getStudentNum(): Int {
        return studentlist.size
    }

    fun moveUp(n: Int) {
        if (index - n < 0) {
            println("Error 100")
        } else {
            index -= n
        }
    }

    fun moveDown(n: Int) {
        if (index + n >= studentlist.size) {
            println("Error 100")
        } else {
            index += n
        }
    }

    fun delete() {
        stack.add(Pair(studentlist[index], index))
        studentlist.removeAt(index)
        if (index == studentlist.size) {
            index--
        }
    }

    fun restore() {
        if (stack.isEmpty()) {
            println("Error 200")
        } else {
            var pair = stack[stack.size - 1]
            if (pair.second <= index) {
                index++
            }
            studentlist.add(pair.second, pair.first)
            stack.removeAt(stack.size - 1)
        }
    }

    fun list() {
        for (student in studentlist) {
            println(student)
        }
    }

    fun quit() {

    }
}