package com.wafflestudio.seminar.test

import java.io.*
import java.util.*

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))
    val studentInput = br.readLine()
    val solver = Solver()
    
    if (studentInput == "q") return
    solver.processStudentInput(studentInput)
    while (true) {
        val input = br.readLine()
        if (input == "q") {
            break
        }
        solver.commandInput(input)
    }
}

class Solver {

    var currentIndex = 0
    var trashCan = Stack<ArrayList<String>>()
    var students = ArrayList<String>()
    fun commandInput(input: String?) {
        val st = StringTokenizer(input)
        val command = st.nextToken()
        if (command[0] == 'm') {
            val direction = st.nextToken()[1]
            val distance = st.nextToken().toInt()
            move(direction, distance)
        } else {
            when (command) {
                "delete" -> {
                    deleteStudent()
                    return
                }
                "restore" -> {
                    restoreStudnet()
                    return
                }
                "list" -> {
                    listStudents()
                    return
                }
                else -> return
            }
        }
    }

    fun move(direction: Char, distance: Int) {
        if (direction == 'u') {
            if (currentIndex - distance >= 0) {
                moveUp(distance)
            } else {
                println("Error 100")
            }
        } else {
            if (currentIndex + distance < students.size) {
                moveDown(distance)
            } else {
                println("Error 100")
            }
        }
    }

    fun moveUp(distance: Int) {
        currentIndex = currentIndex - distance
    }

    fun moveDown(distance: Int) {
        currentIndex = currentIndex + distance
    }

    fun deleteStudent() {
        trashCan.push(copyStudentArray())
        students.removeAt(currentIndex)
        if (currentIndex == students.size) {
            currentIndex--
        }
    }

    fun copyStudentArray(): ArrayList<String> {
        val coppiedStudents = ArrayList<String>()
        for (student in students) {
            coppiedStudents.add(student)
        }
        return coppiedStudents
    }

    fun restoreStudnet() {
        if (trashCan.size >= 1) {
            val currenString = students[currentIndex]
            students = trashCan.pop()
            if (currenString != students[currentIndex]) {
                currentIndex++
            }
        } else {
            println("Error 200")
        }
    }

    fun listStudents() {
        for (student in students) {
            println(student)
        }
    }

    fun processStudentInput(input: String) {
        val processedData = input.substring(1, input.length - 1)
        val processedArray = processedData.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (str in processedArray) {
            students.add(str.substring(1, str.length - 1))
        }
    }
}
