package com.wafflestudio.seminar.test

import java.util.*
import kotlin.collections.ArrayList

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
class Solution(
    private var stdlst : ArrayList<String>,) {
     var history = Stack<ArrayList<String>>()
    var cursor: String = stdlst[0]
    fun move(dir: String, num: Int) {
        if (dir == "u") {
            if (stdlst.indexOf(cursor) - num < 0){
                println("Error 100")
                return
            }else{
                cursor = stdlst[stdlst.indexOf(cursor) - num]
                return
            }
        }else{
            if(stdlst.indexOf(cursor) + num >= stdlst.size){
                println("Error 100")
            }else{
                cursor = stdlst[stdlst.indexOf(cursor) + num]
                return
            }
        }
    }
    fun delete(){
        val deletenum: Int = stdlst.indexOf(cursor)
        if(deletenum == stdlst.size-1){
            var copy = ArrayList<String>()
            copy.addAll(stdlst)
            history.push(copy)
            cursor = stdlst[stdlst.indexOf(cursor)-1]
            stdlst.remove(stdlst[deletenum])
        }else{
            var copy = ArrayList<String>()
            copy.addAll(stdlst)
            history.push(copy)
            cursor = stdlst[stdlst.indexOf(cursor)+1]
            stdlst.remove(stdlst[deletenum])
        }
    }
    fun restore(){
        if(history.isEmpty()){
            println("Error 200")
            return
        }else{
            stdlst = history.pop()
        }
    }
    fun lst(): ArrayList<String>{
        return stdlst
    }
}
fun main() {
    val sc: Scanner = Scanner(System.`in`)
    var order = sc.nextLine()
    order = order.substring(1,order.length-1)
    val orderarrlst = order.split(",").toCollection(ArrayList<String>())
    var solution = Solution(orderarrlst)
    
    while(true){
        order = sc.nextLine()
        val orderarr = order.split(" ")
        if(orderarr[0] == "move"){
            if(orderarr[1] == "-d"){
                solution.move("d",  orderarr[2].toInt())
            }else if(orderarr[1] == "-u"){
                solution.move("u", orderarr[2].toInt())
            }
        }else if(orderarr[0] == "delete"){
            solution.delete()
        }else if(orderarr[0] == "restore"){
            solution.restore()
        }else if(orderarr[0] == "list"){
            val students = solution.lst()
            for(std in students){
                println(std.substring(1,std.length-1))
                
            }
        }else if(orderarr[0] == "q"){
            break
        }
    }
}