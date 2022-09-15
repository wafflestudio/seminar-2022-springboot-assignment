package com.wafflestudio.seminar.test
import java.util.*

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
fun main() {
    // 여기를 채워 주세요!
    
    val sc = Scanner(System.`in`)
    val studentList = StudentList()
    studentList.initialize(sc)
    
    while (true){
        var option: String = sc.next()
        
        if (option == "q") {
            break
        } else if (option == "move") {
            val direction = sc.next()
            val num = sc.nextInt()
            studentList.move(direction, num)
        } else if (option == "delete") {
            studentList.delete()
        } else if (option == "restore") {
            studentList.restore()
        } else if (option == "list") {
            studentList.list()
        }
        
    }
    
    
}

class StudentList{

    var studentList: MutableList<String> = mutableListOf()
    var selectedNum: Int = 0

    var deletedNameList: MutableList<String> = mutableListOf()
    var deletedNumList: MutableList<Int> = mutableListOf()
    
    fun initialize(sc: Scanner){
        var input : String = sc.nextLine()
        input = input.replace("[\"\\[\\]]".toRegex() , "")
        studentList = input.split(",").toMutableList()
    }
    
    fun move(direction: String, num: Int){
        if (direction == "-u") {
            var tempSelectedNum = selectedNum - num
            if (tempSelectedNum < 0) {
                println("Error 100")
            } else {
                selectedNum = tempSelectedNum
            }
        } else {
            var tempSelectedNum = selectedNum + num
            if (tempSelectedNum > studentList.size - 1) {
                println("Error 100")
            } else {
                selectedNum = tempSelectedNum
            }
        }
    }
    
    fun delete(){
        deletedNumList.add(selectedNum)
        deletedNameList.add(studentList[selectedNum])
        
        studentList.removeAt(selectedNum)
        if (selectedNum == studentList.size){
            selectedNum -= 1
        }
    }
    
    fun restore(){
        if (deletedNameList.isEmpty()){
            println("Error 200")
        } else {
            var deletedCount : Int = deletedNameList.size - 1
            var restoredNum : Int = deletedNumList.removeAt(deletedCount)
            var restoredName : String = deletedNameList.removeAt(deletedCount)
            
            studentList.add(restoredNum, restoredName)
            
            if (restoredNum <= selectedNum){
                selectedNum += 1
            }
        }
    }
    
    fun list(){
        studentList.forEach{println(it)}
    }
}