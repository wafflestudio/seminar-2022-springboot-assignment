package com.wafflestudio.seminar.test

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
fun main() {
    val inputList: String = readLine().toString()
    val regex = Regex("[a-zA-Z]+")
    var inputList0 = regex.findAll(inputList)
    var studentList: MutableList<String> = mutableListOf()
    for (student in inputList0){
        studentList.add(student.value)
    }
    student_management(studentList)
}

fun student_management(studentList: MutableList<String>): Unit {
    var deleteList: MutableList<MutableList<Any>> = mutableListOf()
    var i: Int = 0
    while (true){
        var command: String = readLine().toString()
        if ("move -u" in command){
            val regex1 = Regex("[0-9]+")
            var numberLs0 = regex1.findAll(command)
            var numberLs: MutableList<Int> = mutableListOf()
            for (num in numberLs0){
                numberLs.add(num.value.toInt())
            }
            var number: Int = numberLs[0]
            if (i-number < 0){
                println("Error 100")
            } else{
                i -= number
            }
        } else if ("move -d" in command){
            val regex1 = Regex("[0-9]+")
            var numberLs0 = regex1.findAll(command)
            var numberLs: MutableList<Int> = mutableListOf()
            for (num in numberLs0){
                numberLs.add(num.value.toInt())
            }
            var number: Int = numberLs[0]
            if ((i+number) > (studentList.size-1)){
                println("Error 100")
            } else{
                i += number
            }
        } else if ("delete" in command){
            if (i == studentList.size-1){
                i -= 1
                deleteList.add(mutableListOf(studentList[i+1],i+1))
                studentList.removeAt(i+1)
            } else{
                deleteList.add(mutableListOf(studentList[i],i))
                studentList.removeAt(i)
            }
        } else if ("restore" in command){
            if (deleteList.size == 0){
                println("Error 200")
            } else{
                var deleteList1: MutableList<Any> = deleteList.last()
                var deleteList10 = deleteList1[0] as String
                var deleteList11 = deleteList1[1] as Int
                if (deleteList11 <= i){
                    i += 1
                }
                studentList.add(deleteList11,deleteList10)
                deleteList.removeLast()
            }
        } else if ("list" in command){
            for (name in studentList){
                println("${name}")
            }
        } else if ("q" in command){
            break
        }
    }
}