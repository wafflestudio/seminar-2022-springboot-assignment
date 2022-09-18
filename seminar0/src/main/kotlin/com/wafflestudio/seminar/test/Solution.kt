package com.wafflestudio.seminar.test

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
/*
커맨드 패턴 사용하면 좋을 거 같음
 */

class Info(var id: String, var idx: Int)

fun main() {
    // 여기를 채워 주세요!
    val students: MutableList<String> = ArrayList()
    val logs = Stack<Info>()
    var cur = 0
    
    // Read Students
    val br = BufferedReader(InputStreamReader(System.`in`))
    val line = br.readLine()
    val regex = "[A-Za-z]+".toRegex()
    val results = regex.findAll(line)
    results?.forEach { 
        students.add(it.value)
    }
    
    // Command
    while (true) {
        val input = br.readLine().split(" ")
        val cmd = input[0]
        
        when(cmd){
            "q" -> break
            "delete" -> {
                val deleteId = students.removeAt(cur)
                logs.push(Info(deleteId, cur))
                if (cur > students.size - 1) cur = students.size - 1
            }
            "restore" -> {
                if (logs.isEmpty()) {
                    println("Error 200")
                    continue
                }
                val lastLog = logs.pop()
                val restoreId = lastLog.id
                val restoreIdx = lastLog.idx
                students.add(restoreIdx, restoreId)
                if (restoreIdx <= cur) cur += 1
            }
            "list" -> students.forEach { id -> println(id) }
            "move" -> {
                val option = input[1]
                val move = input[2].toInt()
                when (option) {
                    "-u" -> if (cur - move < 0) println("Error 100") else cur -= move
                    "-d" -> if (cur + move > students.size - 1) println("Error 100") else cur += move
                }
            }
        }
    }
}