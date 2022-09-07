package com.wafflestudio.seminar.test

private const val MOVE = "move"
private const val DELETE = "delete"
private const val RESTORE = "restore"
private const val LIST = "list"
private const val QUIT = "q"

fun main() {
    val nameList = parseInput(readln())
    val tableManager = TableManager(Table(nameList))

    while (true) {
        val input = readln().split(" ")
        val command = input[0]
        
        when (command) {
            MOVE -> tableManager.move(input[1][1], input[2].toInt())
            DELETE -> tableManager.delete()
            RESTORE -> tableManager.restore()
            LIST -> tableManager.list()
            QUIT -> break
        }
    }
}

fun parseInput(_input: String): List<String> {
    val input = _input.trim()
    
    return input.substring(1, input.length - 1)
        .split(",")
        .map { s -> s.replace("\"", "") }
}
