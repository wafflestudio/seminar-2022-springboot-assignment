package com.wafflestudio.seminar.test

import java.util.Stack
import kotlin.system.exitProcess

/**
 * TODO 
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
fun main() {
    // 여기를 채워 주세요!
    // Get Students List
    val students: List<String> = makeList(readln())
    val studentList = StudentList(students)
    
    while (true) {
        val decoded: Pair<Command, Int?> = decode(readln())
        execute(decoded, studentList)
    }
}

fun makeList(input: String): List<String> {
    return input.substring(1 .. input.length-2).split(",").map {it.substring(1..it.length-2)}
}

fun decode(input: String): Pair<Command, Int?> {
    val splitted = input.split(" ")
    val cmdNum: Pair<Command, Int?> = run {
        val cmd: Command
        var num: Int? = null
        when (splitted[0]) {
            "move" -> {
                cmd = Command.MOVE
                num = when (splitted[1]) {
                    "-u" -> -1 * splitted[2].toInt()
                    "-d" -> splitted[2].toInt()
                    else -> throw Error()
                }
            } "delete" -> {
                cmd = Command.DELETE
            } "list" -> {
                cmd = Command.LIST
            } "restore" -> {
                cmd = Command.RESTORE
            } "q" -> {
                cmd = Command.EXIT
            } else -> {
                throw Error()
            }
        }
        Pair(cmd, num)
    }
    
    return cmdNum
}
fun execute(decoded: Pair<Command, Int?>, studentList: StudentList) {
    when (decoded.first) {
        Command.MOVE -> {
            studentList.move(decoded.second!!)
        }
        Command.DELETE -> {
            studentList.delete()
        }
        Command.RESTORE -> {
            studentList.restore()
        }
        Command.LIST -> {
            studentList.list()
        }
        Command.EXIT -> {
            exitProcess(0)
        }
    }
}

enum class Command {
    MOVE, DELETE, RESTORE, LIST, EXIT;
}

class StudentList(val students: List<String>) {
    val deletionMarks = MutableList<Boolean>(students.size, {false})
    val deletedIdx = Stack<Int>()
    var currentIdx = 0
    var minusCnt = 0
    var plusCnt = students.size - 1
    
    fun move(num: Int) {
        var num = num
        if (num < 0) {
            if (minusCnt < -num) { 
                println("Error 100")
            } else {
                minusCnt += num
                plusCnt -= num
                while (num != 0) {
                    currentIdx--
                    if (!deletionMarks[currentIdx]) {
                        num++
                    }
                }
            }
        } else if (num > 0) {
            if (plusCnt < num) { 
                println("Error 100")
            } else {
                minusCnt += num
                plusCnt -= num
                while (num != 0) {
                    currentIdx++
                    if (!deletionMarks[currentIdx]) {
                        num--
                    }
                }
            }
        }
    }

    fun delete() {
        deletedIdx.push(currentIdx)
        deletionMarks[currentIdx] = true
        if (plusCnt > 0) {
            plusCnt--
            while (deletionMarks[++currentIdx]){}
        } else {
            minusCnt--
            while (deletionMarks[--currentIdx]){}
        }
    }

    fun restore() {
        if (deletedIdx.isNotEmpty()) {
            val restoreIdx = deletedIdx.pop()
            deletionMarks[restoreIdx] = false
            if (restoreIdx > currentIdx) {
                plusCnt++
            } else if (restoreIdx < currentIdx) {
                minusCnt++
            }
        } else {
            println("Error 200")
        }
    }

    fun list() {
        students.forEachIndexed {
            i, name -> if (!deletionMarks[i]) { println(name) }
        }
    }
}
