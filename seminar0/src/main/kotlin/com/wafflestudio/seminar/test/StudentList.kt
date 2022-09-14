package com.wafflestudio.seminar.test

import java.util.*

class StudentList(private var studentList: MutableList<Pair<String, Boolean>>) {
    private val size = studentList.size
    private var currPosition = 0
    private var deleteStack = Stack<Int>()
    fun command(cmd: List<String>) {
        when (cmd[0]) {
            "move" -> move(cmd[1], cmd[2].toInt())
            "delete" -> delete()
            "restore" -> restore()
            "list" -> list()
        }
    }

    fun move(direction: String, number: Int) {
        when (direction) {
            "-u" -> move(-1, number)
            "-d" -> move(1, number)
        }
    }

    fun move(direction: Int, number: Int) {
        var count = 0
        var tempPosition = currPosition
        while (count < number) {
            tempPosition += direction
            if (tempPosition >= size || tempPosition < 0) {
                println("Error 100")
                break
            }
            if (studentList[tempPosition].second) count++
        }
        currPosition = tempPosition
    }

    fun delete() {
        studentList[currPosition] = studentList[currPosition].copy(second = false)
        deleteStack.push(currPosition)
        try {
            move("-d", 1)
        } catch (e: IllegalArgumentException) {
            move("-u", 1)
        }
    }

    fun restore() {
        try {
            currPosition = deleteStack.pop()
            studentList[currPosition] = studentList[currPosition].copy(second = true)
        } catch (e: EmptyStackException) {
            println("Error 200")
        }
    }

    fun list() {
        studentList.filter { it.second }
            .forEach { println(it.first) }
    }
}