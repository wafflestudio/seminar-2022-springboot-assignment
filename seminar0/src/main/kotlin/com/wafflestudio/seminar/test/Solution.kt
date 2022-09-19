package com.wafflestudio.seminar.test

class Student(name: String){
    val name: String = name
    var exist: Boolean = true
}

fun main() {
    val std: MutableList<Student> = mutableListOf()
    var temp = readln()
    temp = temp.substring(1,temp.length - 1)
    val list = temp.split(",")
    var totNum = 0
    for(l in list){
        std.add(totNum, Student(l.substring(1,l.length - 1)))
        totNum++
    }
    var now = 0
    var input:String
    var delete:MutableList<Int> = mutableListOf()
    
    while(true){
        input = readln()
        if(input == "q")
            break
        val inputList = input.split(' ')
        if(inputList[0] == "move"){
            if(inputList[1] == "-u"){
                val move = inputList[2].toInt()
                if(move < 0){
                    println("Error 100")
                    continue
                }
                if(move == 0)
                    continue
                var loc = now
                var num = 0
                while(loc > 0 && num < move) {
                    loc--
                    if (std[loc].exist)
                        num++
                }
                if(num == move)
                    now = loc
                else
                    println("Error 100")
            }
            else if(inputList[1] == "-d"){
                val move = inputList[2].toInt()
                if(move < 0) {
                    println("Error 100")
                    continue
                }
                if(move == 0)
                    continue
                var loc = now
                var num = 0
                while(loc < totNum - 1 && num < move){
                    loc++
                    if(std[loc].exist)
                        num++
                }
                if(num == move)
                    now = loc
                else
                    println("Error 100")
            }
            else{
                println("Wrong move direction.")
            }
        }
        
        else if(inputList[0] == "delete"){
            std[now].exist = false
            delete.add(delete.size, now)
            var loc = now + 1
            while(loc < totNum){
                if(std[loc].exist){
                    now = loc
                    break
                }
                loc++
            }
            if(now != loc){
                while(std[--now].exist == false){}
            }
        }
        
        else if(inputList[0] == "restore"){
            if(delete.isEmpty())
                println("Error 200")
            else {
                std[delete[delete.size - 1]].exist = true
                delete.removeAt(delete.size - 1)
            }
        }
        
        else if(inputList[0] == "list"){
            for(s in std){
                if(s.exist)
                    println(s.name)
            }
        }
        
        else{
            println("Wrong action.")
        }
        
    }
}