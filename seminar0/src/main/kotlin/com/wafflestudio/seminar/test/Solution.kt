package com.wafflestudio.seminar.test

class Table(
    private val table: MutableList<String>,
    private var line: Int,
): Cloneable {
    public override fun clone(): Table {
        return Table(table.toMutableList(), line)
    }
    fun getTarget(): String {
        return table[line]
    }
    
    fun setLineByTarget(target: String) {
        line = table.indexOf(target)
    }
    
    fun printTable() {
        for (i in table) { println(i) }
    }
    
    fun moveLine(option: String, moveLine: Int) {
        when (option) {
            "-u" -> {
                if (line - moveLine >= 0) line -= moveLine
                else println("Error 100")
            }
            "-d" -> {
                if (line + moveLine < table.size) line += moveLine
                else println("Error 100")
            }
        }
    }
    
    fun deleteLine() {
        table.removeAt(line)
        if (line == table.size) line -= 1
    }
}

fun main() {
    val tableHistory: MutableList<Table> = mutableListOf()
    
    val input: String? = readLine()
    val data: List<String> = input!!
        .substring(1, input.length - 1)
        .filter { it != '"' }
        .split(",")
    var table = Table(data.toMutableList(), 0)
    
    while (true) {
        val command: String? = readLine()
        if (command == "q") break
        val commandSplit: List<String> = command!!.split(" ")
        when (commandSplit[0]) {
            "list" -> table.printTable()
            "move" -> table.moveLine(commandSplit[1], commandSplit[2].toInt())
            "delete" -> {
                tableHistory.add(table.clone())
                table.deleteLine()
            }
            "restore" -> {
                if (tableHistory.size > 0) {
                    val target: String = table.getTarget()
                    table = tableHistory[tableHistory.size - 1]
                    table.setLineByTarget(target)
                    tableHistory.removeAt(tableHistory.size - 1)
                } else println("Error 200")
            }
        } 
    }
}