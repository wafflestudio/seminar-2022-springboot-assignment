package com.wafflestudio.seminar.test

import java.util.*

class Table(studentList: List<String>) {
    private var studentList: MutableList<StudentInfo>
    init {
        this.studentList = studentList.mapIndexed { i, name -> StudentInfo(i, name) }.toMutableList()
    }
    private var recycleBin = Stack<StudentInfo>()
    private var curr: Int = 0
    fun move(dir: String, count: Int){
        when(dir){
            "-u" -> { if (count > curr) { println("Error 100")
            } else{
                curr -= count
            }
            }
            "-d" -> { if (count + curr >= studentList.size){
                println("Error 100")
                } else {
                    curr += count
            }
            }
        }
    }
    fun delete(){
        studentList[curr].id = curr
        recycleBin.push(studentList[curr])
        studentList.removeAt(curr)
        if(curr == studentList.size){
            curr -= 1
        }
    }
    
    fun restore(){
        if (recycleBin.isEmpty()){
            println("Error 200")
        }else{
            val bin = recycleBin.pop()
            studentList.add(bin.id, bin)
            if(bin.id <= curr){
                curr += 1
            }
        }
    }
    
    fun list(){
        for(i in studentList){
            println(i.name)
        }
    }
    
}