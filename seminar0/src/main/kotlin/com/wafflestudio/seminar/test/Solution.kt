package com.wafflestudio.seminar.test
import java.util.*
import kotlin.collections.ArrayList


class StudentDB {
    private var studentList : MutableList<String> = ArrayList()
    private var deletedStudentStack: Stack<DeleteStackItem> = Stack()
    private var currentIdx: Int = 0

    companion object {
        const val INDEX_ERROR_CODE: String = "Error 100"
        const val EMPTY_RESTORE_CODE: String = "Error 200"
    }

    class DeleteStackItem(public val name: String, public val index: Int) {}

    fun setStudentList(command: String) {
        command.substring(1, command.length - 1)
            .split(",")
            .forEach{studentList.add(it.replace("\"", ""))}
    }

    fun executeQuery(query: String) {
        val querySplit: List<String> = listOf(*query.split(" ").toTypedArray())
        if (querySplit.size == 3) {
            move(flag=querySplit[1], nMove=querySplit[2].toInt())
        } else {
            when (querySplit[0]) {
                "list" -> list()
                "delete" -> delete()
                "restore" -> restore()
            }
        }
    }

    fun move(flag: String, nMove: Int) {
        when (flag) {
            "-u" -> {
                if (currentIdx - nMove < 0) println(INDEX_ERROR_CODE)
                else currentIdx -= nMove
            }
            "-d" -> {
                if (currentIdx + nMove >= studentList.size) println(INDEX_ERROR_CODE)
                else currentIdx += nMove
            }
        }

    }

    fun list() {
        println(studentList.joinToString(prefix="", separator = "\n", postfix=""))
    }

    fun delete() {
        deletedStudentStack.add(DeleteStackItem(studentList.removeAt(currentIdx), currentIdx))
        if (currentIdx == studentList.size) currentIdx -= 1
    }

    fun restore() {
        if (deletedStudentStack.size == 0) println(EMPTY_RESTORE_CODE)
        else {
            val deleteStackTop: DeleteStackItem = deletedStudentStack.pop()
            studentList.add(deleteStackTop.index, deleteStackTop.name)
            if (deleteStackTop.index <= currentIdx) currentIdx += 1
        }
    }

}


fun main() {
    var studentListSet: Boolean = false
    val db : StudentDB = StudentDB()

    while (true) {
        val command: String  = readLine()!!
        if (!studentListSet) {
            db.setStudentList(command)
            studentListSet = true
        }
        else if (command == "q") {
            break
        } else {
            db.executeQuery(command)
        }
    }
}