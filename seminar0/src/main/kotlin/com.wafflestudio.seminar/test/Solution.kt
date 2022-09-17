package com.wafflestudio.seminar.test

import java.util.LinkedList

fun main() {
    val students = readLine()!!
        // remove [ ]
        .drop(1).dropLast(1)
        .split(",")
        // remove " " for each element
        .map { it.drop(1).dropLast(1) }
        .toMutableList()

    val table = StudentTable(LinkedList(students))
    while (table.execute(command = readln())) { continue }
}

class StudentTable(
    private val students: LinkedList<String>
) {
    private var cursor = 0
    private val cache = mutableListOf<Cached>()

    private data class Cached(
        val originalIndex: Int,
        val studentName: String,
    )

    fun execute(command: String): Boolean {
        val parsed = command.split(" ")
        val cmdInput = parsed.first()
        val args = parsed.drop(1)

        when (cmdInput) {
            "move" -> move(args)
            "delete" -> delete()
            "restore" -> restore()
            "list" -> list()
            "q" -> return false
        }

        return true
    }

    private fun move(args: List<String>) {
        val (opt, count) = args
        when (opt) {
            "-u" -> moveUp(count.toInt())
            "-d" -> moveDown(count.toInt())
            else -> throw IllegalArgumentException()
        }
    }

    private fun moveUp(count: Int) {
        val resultLoc = cursor - count
        if (resultLoc < 0) {
            println("Error 100")
            return
        }

        cursor = resultLoc
    }

    private fun moveDown(count: Int) {
        val resultLoc = cursor + count
        if (resultLoc >= students.size) {
            println("Error 100")
            return
        }

        cursor = resultLoc
    }

    private fun delete() {
        val removed = this.students.removeAt(cursor)
        this.cache.add(Cached(cursor, removed))

        // now current Index has moved to it's next one
        // reallocate current index if no next exists
        if (cursor == students.size) {
            cursor -= 1
        }
    }

    private fun restore() {
        val cached = this.cache.removeLastOrNull() ?: run {
            println("Error 200")
            return
        }

        this.students.add(cached.originalIndex, cached.studentName)

        // if current Index was goe than cached.originalIndex, increment
        if (cursor >= cached.originalIndex) {
            cursor += 1
        }
    }

    private fun list() {
        students.forEach(::println)
    }
}