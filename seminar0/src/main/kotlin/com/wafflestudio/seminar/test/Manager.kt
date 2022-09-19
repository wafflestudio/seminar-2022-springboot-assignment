package com.wafflestudio.seminar.test

import java.util.Stack

class Manager<T>(list: List<T>) : ManagerInterface 
{
    private var table: MutableList<T>
    private var deleteHistory: Stack<Pair<Int, T>> = Stack()
    private var cursor: Int = 0
    
    init {
        table = list.toMutableList()
    }
    
    override fun move(amt: Int) {
        val nextCursor = cursor + amt
        
        if (nextCursor < 0 || table.size <= nextCursor) {
            print("Error 100\n")
            return
        }
        
        cursor = nextCursor
    }

    override fun delete() {
        if (table.size == 1) {
            print("Cannot delete more")
        }
        
        deleteHistory.push(Pair(cursor, table.removeAt(cursor)))
        
        if (cursor == table.size) cursor--
    }

    override fun restore() {
        if (deleteHistory.isEmpty()) {
            print("Error 200\n")
            return
        }
        
        val pair = deleteHistory.pop()
        table.add(pair.first, pair.second)
        
        if (pair.first <= cursor) cursor++
    }

    override fun list() {
        for (item in table) {
            print(item.toString() + "\n")
        }
    }

}
