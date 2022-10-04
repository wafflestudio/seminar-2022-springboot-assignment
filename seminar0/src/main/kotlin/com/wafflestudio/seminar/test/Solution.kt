package com.wafflestudio.seminar.test

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */

class DataSet() {
    private val recycleBinName : MutableList<String> = mutableListOf()
    private val recycleBinIndex : MutableList<Int> = mutableListOf()
    var size : Int = 0
    var cursor : Int = 0
    private val systemUsers : MutableList<String> = mutableListOf()
    private val sudoers : List<String> = systemUsers

    fun addSystemUser(newUser : String){
        systemUsers.add(newUser)
    }

    fun deleteSystemUser(){
        recycleBinName.add(getSysSudoers()[cursor])
        recycleBinIndex.add(cursor)
        systemUsers.removeAt(cursor)
        if(cursor == size - 1){
            cursor--
        }
        size--
    }

    fun restoreSystemUser(){
        if(recycleBinName.size == 0){
            println("Error 200")
        }else{
            if(cursor >= recycleBinIndex.last()){
                cursor++
            }
            systemUsers.add(recycleBinIndex.last(), recycleBinName.last())
            recycleBinName.removeLast()
            recycleBinIndex.removeLast()
            size++
        }


    }

    fun getSysSudoers(): List<String>{
        return sudoers
    }

}

fun main() {
    val dataSet = DataSet()

    val br = BufferedReader(InputStreamReader(System.`in`))
    var input = br.readLine()
    input = input.replace("\"", "")
    input = input.replace("[", "")
    input = input.replace("]", "")
    input = input.replace("\\s".toRegex(), "")

    val dataBase = input.split(",").toTypedArray()
    for(x in dataBase){
        dataSet.addSystemUser(x)
        dataSet.size++
    }
    var cmd : String

    do {
        cmd = br.readLine();
        command(cmd, dataSet)
    } while (cmd != "q")

    br.close()

}

fun command(cmd : String, dataSet : DataSet){
    if(cmd == "list"){
        for(name in dataSet.getSysSudoers()){
            println(name)
        }
    }
    if(cmd.contains("move", ignoreCase = true)){
        val move = cmd.split(" ")
        if(cmd.contains("-u", ignoreCase = true)){
            if(dataSet.cursor - move[2].toInt() < 0){
                println("Error 100")
            }else{
                dataSet.cursor = dataSet.cursor - move[2].toInt()
            }
        }else if(cmd.contains("-d", ignoreCase = true)){
            if(dataSet.cursor + move[2].toInt() > dataSet.size - 1){
                println("Error 100")
            }else{
                dataSet.cursor = dataSet.cursor + move[2].toInt()
            }
        }
    }
    if(cmd.contains("delete", ignoreCase = true)){
        dataSet.deleteSystemUser()
    }

    if(cmd.contains("restore", ignoreCase = true)){
        dataSet.restoreSystemUser()
    }


}