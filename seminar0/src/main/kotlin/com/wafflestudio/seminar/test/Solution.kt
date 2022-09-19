package com.wafflestudio.seminar.test

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */

fun parseTipInteger(line : String) : Int
{
    val splitLine = line.split(' ');
    return splitLine.last().toInt();
}
fun main() {
    val firstLine = readLine();
    val table = Table(firstLine);

    // 여기를 채워 주세요!
    while(true) {
        val line = readLine() ?: ""
        if (line == "q")
            break;
        else if (line == "list") {
            table.printAll()
        } else if (line == "delete") {
            table.delete()
        } else if (line == "restore") {
            if(!table.restore())
            {
                println("Error 200")
            }
        } else if (line.contains("move")) {
            val movement: Int = if (line.contains('u')) {
                -parseTipInteger(line);
            } else {
                parseTipInteger(line);
            }

            if(!table.moveIndex(movement))
            {
                println("Error 100")
            }
        }
    }
}