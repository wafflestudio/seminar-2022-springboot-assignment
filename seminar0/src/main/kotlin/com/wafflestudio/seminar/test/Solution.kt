package com.wafflestudio.seminar.test

fun main() {
    val manager: ManagerInterface = Manager(parseInputLine(readln()))
    while (true) {
        when (runCommand(manager, readln())) {
            RunCommandResult.SUCCESS -> continue
            RunCommandResult.ERROR -> print("Invalid Command\n")
            RunCommandResult.QUIT -> break
        }
    }
}

fun parseInputLine(line: String): List<StudentInfo> {
    return line
        .split("[^a-z]*\"[^a-z]*".toRegex())
        .filter { str -> str.isNotEmpty() }
        .map { str -> StudentInfo(str) }
}

fun runCommand(manager: ManagerInterface, line: String): RunCommandResult {
    val command = line.split("[ \t]+".toRegex())
    
    when (command.size) {
        1 -> {
            when (command[0]) {
                "delete" -> manager.delete()
                "restore" -> manager.restore()
                "list" -> manager.list()
                "q" -> return RunCommandResult.QUIT
                else -> return RunCommandResult.ERROR
            }
        }
        3 -> {
            if (command[0] != "move") return RunCommandResult.ERROR

            val amount: Int = try {
                command[2].toInt()
            } catch (_: NumberFormatException) {
                return RunCommandResult.ERROR
            }

            manager.move(
                when (command[1]) {
                    "-u" -> -amount
                    "-d" -> amount
                    else -> return RunCommandResult.ERROR
                }
            )
        }
        else -> {
            return RunCommandResult.ERROR
        }
    }
    
    return RunCommandResult.SUCCESS
}

enum class RunCommandResult {
    SUCCESS, ERROR, QUIT
}

data class StudentInfo(val name: String) {
    override fun toString(): String = name
}

interface ManagerInterface {
    fun move(amt: Int)
    fun delete()
    fun restore()
    fun list()
}
