package com.wafflestudio.seminar.test

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
var pointer: Int = 0
val list = mutableListOf<String>()
val bin = mutableListOf<DeletedStudent>()

data class DeletedStudent(val name: String, val location: Int){}
fun printController(str: String){
    println(str)
}
fun move(sign: Int, amount: Int){
    val next = pointer + amount * sign
    if(list.size - 1 < next || next < 0){
        printController("Error 100")
    }
    else {
        pointer = next
    }
}
fun delete(){
    val student = list.get(pointer); list.removeAt(pointer)
    bin.add(DeletedStudent(student, pointer))
    if(pointer == list.size){
        pointer--
    }
    
}

fun restore(){
    // Error 처리
    if(bin.isEmpty()){
        printController("Error 200")
        return
    }
    
    val lastOne : DeletedStudent = bin.get(bin.size - 1)
    bin.removeAt(bin.size - 1)
    list.add(lastOne.location, lastOne.name)
    
    if(pointer >= lastOne.location){
        pointer++
    }
    
}


fun main() {
    // initialize
    list.clear()
    bin.clear()
    pointer = 0
    
    val regex = "\\w+".toRegex()
    val str = readln()
    val matchResults: Sequence<MatchResult> = regex.findAll(str)
    matchResults?.forEach{
        list.add(it.value)
    }
    
    while(true){
        val input = readln().split(" ")
        when(input[0]){
            "move" -> 
                if(input[1] == "-u"){
                    move(-1, input[2].toInt())
                }else {
                    move(1, input[2].toInt())
                }
            "delete" -> delete()
            "restore" -> restore()
            "list" -> list.forEach{printController(it)}
            "q" -> break
        }
    }
    
    
    
}