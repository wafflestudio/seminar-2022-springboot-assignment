package com.wafflestudio.seminar.test

class Table(nameList: List<String>) {
    private val studentList: MutableList<Student>

    init {
        studentList = nameList.mapIndexed { idx, name -> Student(idx, name) }.toMutableList()
    }
    
    val size: Int
        get() = studentList.size

    fun remove(cursor: Cursor): Student = studentList.removeAt(cursor.index)
    fun printAll() = studentList.forEach { println(it.name) }

    fun restore(restoredStudent: Student): Int {
        val insertIdx = findInsertIndex(restoredStudent)
        studentList.add(insertIdx, restoredStudent)
        return insertIdx
    }

    private fun findInsertIndex(restoredStudent: Student): Int {
        var insertIdx = 0
        while (insertIdx < size) {
            if (restoredStudent.place < studentList[insertIdx].place)
                break
            insertIdx++
        }
        return insertIdx
    }
}