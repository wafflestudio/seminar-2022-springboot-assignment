package com.wafflestudio.seminar.test

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
fun main() {
    var students = readLine()!!
    students = students.replace("\"", "")
    students = students.replace("[", "")
    students = students.replace("]", "")
    val students_arr = students.split(",")

    var selected = 0
    var deleted: MutableList<Int> = MutableList(students_arr.size, {0})
    var del_list = mutableListOf<String>()
    var first = 0
    var end = students_arr.size - 1

    while(true){
        var command = readLine()!!.split(" ")
        if (command[0] == "q")
            break
        else if (command[0] == "list"){
            for (i in students_arr.indices){
                if (deleted[i] == 0)
                    println(students_arr[i])
            }
        }
        else if (command[0] == "delete"){
            del_list.add(students_arr[selected])
            deleted[selected] = 1
            if (selected == end){
                selected -= 1
                while (deleted[selected] == 1)
                    selected -= 1
                end = selected
            }
            else if (selected == first){
                while (deleted[selected] == 1)
                    selected += 1
                first = selected
            }
            else{
                while (deleted[selected] == 1)
                    selected += 1
            }
        }
        else if (command[0] == "restore"){
            if (del_list.size == 0)
                println("Error 200")
            else{
                var a = students_arr.indexOf(del_list[del_list.lastIndex])
                deleted[a] = 0
                if (a < first)
                    first = a
                else if (a > end)
                    end = a
                del_list.remove(del_list[del_list.lastIndex])
            }
        }
        else if (command[0] == "move")
        {
            var cnt = 0
            var num = command[2].toInt()
            var temp = selected
            if (command[1] == "-u")
            {
                while (cnt != num)
                {
                    if (temp == first){
                        println("Error 100")
                        break
                    }
                    temp -= 1
                    if (deleted[temp] == 0)
                        cnt += 1
                }
            }
            else if (command[1] == "-d"){
                while (cnt != num){
                    if (temp == end){
                        println("Error 100")
                        break
                    }
                    temp += 1
                    if (deleted[temp] == 0)
                        cnt += 1
                }
            }
            if (cnt == num)
                selected = temp
        }
    }
}