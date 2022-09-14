package com.wafflestudio.seminar.test

import java.util.*

data class Student(var name: String, var valid: Boolean)
class StudentList(private var studentList: MutableList<Student>) {

    private val size = studentList.size
    private var currPosition = 0
    private var deleteStack = Stack<Int>()

    fun command(cmd: List<String>) {
        when (cmd[0]) {
            "move" -> try {
                move(cmd[1], cmd[2].toInt())
            } catch(e: IllegalArgumentException) {
                println(e.message)
            }
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
                throw IllegalArgumentException("Error 100")
            }
            if (studentList[tempPosition].valid) count++
        }
        currPosition = tempPosition
    }

    fun delete() {
        studentList[currPosition].valid = false
        deleteStack.push(currPosition)
        try {
            move("-d", 1)
        } catch (e: IllegalArgumentException) {
            move("-u", 1)
        }
    }

    fun restore() {
        try {
            val currPosition = deleteStack.pop()
            studentList[currPosition].valid = true
        } catch (e: EmptyStackException) {
            println("Error 200")
        }
    }

    fun list() {
        studentList.filter { it.valid }
            .forEach { println(it.name) }
    }
}