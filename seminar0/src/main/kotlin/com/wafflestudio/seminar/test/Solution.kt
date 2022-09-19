package com.wafflestudio.seminar.test

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */

class Students(private val studentList: MutableList<String>){
    var currentIdx = 0
    var tempIdx = 0
    private val deletedIdxList = mutableListOf<Int>()
    private val deletedStudentList = mutableListOf<String>()
    var continueCondition = true

    fun getCommand(command: List<String>){
        if(command.size != 1) {
            move(command[1], command[2].toInt())
        }
        else {
            when (command[0]){
                "delete" -> delete()
                "restore" -> restore()
                "list" -> printList()
                "q" -> continueCondition = false
            }
        }
    }

    private fun move(direction: String, distance: Int){
        if(direction == "-u"){
            tempIdx -= distance
        }
        else{ // direction == "-d"
            tempIdx += distance
        }

        if(tempIdx >= 0 && tempIdx < studentList.size){
            currentIdx = tempIdx
        }
        else{
            tempIdx = currentIdx
            print("Error 100\n")
        }
    }

    private fun delete(){
        deletedIdxList.add(currentIdx)
        deletedStudentList.add(studentList[currentIdx])

        if(currentIdx == studentList.lastIndex){
            studentList.removeAt(currentIdx)
            currentIdx--
        }
        else{
            studentList.removeAt(currentIdx)
        }
    }

    private fun restore(){
        if(deletedStudentList.isNotEmpty()){
            val deletedIdx = deletedIdxList.last()
            deletedIdxList.removeLast()
            val deletedStudent = deletedStudentList.last()
            deletedStudentList.removeLast()
            if(deletedIdx <= currentIdx) currentIdx++
            studentList.add(deletedIdx, deletedStudent)
        } 
        else{
            print("Error 200\n")
        }
    }

    private fun printList(){
        for(student in studentList){
            print("$student\n")
        }
    }
}

fun main(){
    val inputData = readLine()
    val studentList = inputData!!.substring(1, inputData.lastIndex).split(',').map{it.substring(1, it.lastIndex)}.toMutableList()
    val students = Students(studentList)

    while(students.continueCondition){
        students.tempIdx = students.currentIdx
        val command = readLine()!!.split(' ')
        students.getCommand(command)
    }
}