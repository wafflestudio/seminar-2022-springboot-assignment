package com.wafflestudio.seminar.test

import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.NullPointerException
import javax.print.attribute.IntegerSyntax


/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */

class Student(val name: String)

fun main() {

    val br = BufferedReader(InputStreamReader(System.`in`)) 
    var n = 0

    var studentList: MutableList<Student> = ArrayList()

    val deleteStudent: MutableList<Student> = ArrayList()
    val deleteNumber: MutableList<Int> = ArrayList()

    var listCommand = br.readLine()
    val charsToRemove = "\""
    val charsToRemove1 = "["
    val charsToRemove2 = "]"

    listCommand = listCommand.replace(charsToRemove,"").replace(charsToRemove1,"").replace(charsToRemove2,"")


    val listCommandArr = listCommand.split(",");


    for (element in listCommandArr){
        studentList.add(Student(element))
    }

    
    while (true){
        val command = br.readLine() ?: break
        var arr = command.split(" ");

        when(arr[0]){
            "move"-> {
                
                var command2 = arr[2].toInt()
                if (arr[1] == "-u") {

                    n -= command2
                    if (n<0) {
                        println("Error 100");
                        n += command2
                    }
                } else if(arr[1] == "-d"){
                    n += command2;
                    if (n>=studentList.size) {
                        println("Error 100")
                        n -= command2
                    }
                }
                
            }

            "delete"-> {
                deleteStudent.add(studentList.get(n))
                deleteNumber.add(n)
                studentList.removeAt(n)
                if (n == studentList.size){
                    n -= 1
                }
                
            }
            
            "restore" -> {
                if(deleteStudent.isNotEmpty()) {
                    studentList.add(deleteNumber[deleteNumber.size -1], deleteStudent[deleteStudent.size -1])
                    if (deleteNumber[deleteNumber.size -1] <= n) {
                        n +=1;
                    }
                    
                    deleteStudent.removeAt(deleteStudent.size -1)
                    deleteNumber.removeAt(deleteNumber.size -1)
                } else {
                    println("Error 200")
                }
            }
            
            "list"-> {
                for (st in studentList) {
                    println(st.name)
                }
            }
            
            "q" -> {
                break
            }
        }
       
    }
    br.close()

}
