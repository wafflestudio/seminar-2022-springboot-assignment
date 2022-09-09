package com.wafflestudio.seminar.test

class StudentTable {
    private val students = ArrayList<String>()
    private val isErased = ArrayList<Boolean>()
    private val removed = ArrayList<Int>()
    private var pos: Int = 0
    private fun moveOne(next: Boolean): Boolean {
        val current = this.pos
        while(true){
            if(next) this.pos++
            else this.pos --
            if(this.pos == this.isErased.size || this.pos == -1){
                this.pos = current
                return false
            }
            if(!isErased[this.pos]){
                return true
            }
        }
    }
    fun append(name: String) {
        this.students.add(name)
        this.isErased.add(false)
    }
    fun delete() {
        this.isErased[this.pos] = true
        this.removed.add(this.pos)
        if (!moveOne(true)) moveOne(false)
    }

    fun move(dir: String, amount: Int) {
        val next: Boolean = dir == "-d"
        val current = this.pos
        for (i in 1..amount) {
            if (!moveOne(next)) {
                this.pos = current
                println("Error 100")
                return
            }
        }
    }
    fun restore() {
        if (this.removed.isEmpty()) {
            println("Error 200")
        return
        }
        this.isErased[this.removed[this.removed.lastIndex]] = false
        this.removed.removeAt(this.removed.lastIndex)
    }
    fun list() {
        for (i in 0 until this.students.size) {
            if (!this.isErased[i]) {
                println(this.students[i])
            }
        }
    }
}

fun main() {
    val studentTable = StudentTable()
    var input = readln().trim()
    val students = input.substring(2..input.length - 3).split("\",\"")
    for (student in students) {
        studentTable.append(student)
    }
    while (true){
        input = readln()
        val temp = input.trim().split(" ")
        when(temp[0]){
            "move" -> studentTable.move(temp[1],temp[2].toInt())
            "delete" -> studentTable.delete()
            "restore" -> studentTable.restore()
            "list" -> studentTable.list()
            "q" -> return
        }
    }
}

