package com.wafflestudio.seminar.test

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
fun main() {
    // 여기를 채워 주세요!
    val br = BufferedReader(InputStreamReader(System.`in`))
    val list = br.readLine().split("\"")
    var stdlist = mutableListOf<String>()
    val restoreList = Stack<MutableList<String>>()
    val restoreIdx = Stack<Int>()
    for(i in 1 until list.size step 2){
        stdlist.add(list[i])
    }
    var cmd = br.readLine().split(" ")
    var idx = 0;
    while(cmd[0] != "q"){
        when(cmd[0]){
            "move" -> {
                val amt = cmd[2].toInt()
                when(cmd[1]){
                    "-u" -> {
                        if(idx-amt>=0) {
                            idx -= amt
                        }else{
                            println("Error 100")
                        }
                    }
                    "-d" -> {
                        if(idx+amt<stdlist.size) {
                            idx += amt
                        }else{
                            println("Error 100")
                        }
                    }
                }
            }
            "delete" -> {
                val copiedList = ArrayList(stdlist)
                restoreList.push(copiedList)
                restoreIdx.push(idx)
                stdlist.removeAt(idx)
                if (stdlist.size == idx){
                    idx--
                }
            }
            "restore" -> {
                if (restoreList.isEmpty()){
                    println("Error 200")
                    cmd = br.readLine().split(" ")
                    continue
                }
                stdlist = restoreList.pop()
                if(idx >= restoreIdx.pop()){
                    idx++
                }
            }
            else -> {
                for(std in stdlist){
                    println(std)
                }
            }
        }
        cmd = br.readLine().split(" ")
    }
}