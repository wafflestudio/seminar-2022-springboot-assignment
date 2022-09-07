package com.wafflestudio.seminar.test

class RemovedStack {
    
    companion object {
        const val RESTORE_ERROR = "Error 200"
    }
    
    private val stack: MutableList<Student> = mutableListOf()
    
    val isEmpty: Boolean
        get() = stack.isEmpty()
    
    fun push(student: Student) = stack.add(student)
    fun pop() = stack.removeAt(stack.size - 1)

}