package com.wafflestudio.seminar.test

class Cursor {
    
    companion object {
        private const val MOVE_ERROR = "Error 100"
    }

    private var value = 0

    val index: Int
        get() = value

    fun move(step: Int, tableSize: Int) {
        if (isValidMove(step, tableSize)) value += step
        else println(MOVE_ERROR)
    }
    
    private fun isValidMove(step: Int, tableSize: Int): Boolean = (value + step) in 0 until tableSize

    fun isLastIndex(tableSize: Int): Boolean = (value == tableSize - 1)
}