package com.wafflestudio.seminar.test


class TableManager(private val table: Table) {
    
    companion object {
        private const val UP_DIRECTION = 'u'
        private const val DOWN_DIRECTION = 'd'
    }
    
    private val removed: RemovedStack = RemovedStack()
    private val cursor: Cursor = Cursor()
    
    fun move(dir: Char, step: Int) {
       when (dir) {
           UP_DIRECTION -> cursor.move(-step, table.size)
           DOWN_DIRECTION -> cursor.move(step, table.size)
       } 
    }

    fun delete() {
        when {
            cursor.isLastIndex(table.size) -> {
                removed.push(table.remove(cursor))
                cursor.move(-1, table.size)
            }
            else -> removed.push(table.remove(cursor))
        }
    }

    fun restore() {
        when {
            removed.isEmpty -> println(RemovedStack.RESTORE_ERROR)
            else -> {
                val insertIdx = table.restore(removed.pop())
                if (cursor.index >= insertIdx)
                    cursor.move(1, table.size)
            }
        }
    }

    fun list() = table.printAll()
}