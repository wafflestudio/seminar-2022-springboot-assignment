package com.wafflestudio.seminar.test

import java.util.*

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
fun main() {
    val sc = Scanner(System.`in`)
    val str: String = sc.nextLine().removeSurrounding("[", "]").replace("\"", "")
    val table = str.split(",").toMutableList()
    val restore = Stack<Int>()

    val del = MutableList(table.size){false}

    var cur = 0

    while (true) {
        var cmd = sc.next()
        if (cmd == "move") {
            cmd = sc.next()
            var cnt = sc.next().toInt()
            if (cmd == "-u") {
                var newCur = cur
                while (newCur >= 0 && cnt > 0) {
                    newCur--
                    if (newCur < 0) {
                        println("Error 100")
                        break
                    }
                    if (!del[newCur]) cnt--
                }

                if (newCur < 0) {
                    continue
                }

                cur = newCur
            } else {
                var newCur = cur
                while (newCur < table.size && cnt > 0) {
                    newCur++
                    if (newCur >= table.size) {
                        println("Error 100")
                        break
                    }

                    if (!del[newCur]) cnt--
                }

                if (newCur >= table.size) {
                    continue
                }

                cur = newCur
            }
        } else if (cmd == "delete") {
            del.set(cur, true)
            restore.push(cur)

            var newCur = cur
            while (newCur < table.size && del[newCur]) {
                newCur++
            }

            if (newCur >= table.size) {
                newCur = cur
                while (newCur >= 0 && del[newCur]) {
                    newCur--
                }
            }

            cur = newCur

        } else if (cmd == "restore") {

            if (restore.size == 0) {
                println("Error 200")
                continue
            }

            del.set(restore.peek(), false)
            restore.pop()
        } else if (cmd == "list") {
            for (i: Int in 0 until table.size) {
                if (del[i]) continue
                println(table[i])
            }
        } else {
            break
        }
    }
}