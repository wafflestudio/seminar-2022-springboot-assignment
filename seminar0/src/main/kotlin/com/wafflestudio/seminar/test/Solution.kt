package com.wafflestudio.seminar.test

/**
 * TODO 
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
//package org.kotlinlang.play         // 1

fun main(){
    fun move_up(idxx: Int, user : String): Int {
        var idx : Int = idxx
        var intuser : Int = user.substring(8,).toInt()
    
        if (idx-intuser >= 0)
            idx -= intuser
        else
            println("Error 100")
        return idx
    }
    
    fun move_down(idxx: Int, user : String, students : MutableList<String>) : Int {
        var idx : Int = idxx
        var intuser : Int = user.substring(8,).toInt()
        if ((idx+intuser) <= (students.size - 1))
            idx += intuser
        else
            println("Error 100")
        return idx
    }
    
    fun delete(idxx : Int, restore_stack : MutableList<String>,students : MutableList<String>): Int {
        var idx : Int = idxx
        restore_stack.add(students.get(idx))
        if (idx == students.size - 1) {
            students.removeAt(idx)
            idx -= 1
        }
        else {
            students.removeAt(idx)
        }
        return idx
    }
    
    fun restore(idxx : Int, std_idx : MutableMap<String, Int>, restore_stack : MutableList<String>, students : MutableList<String>) : Int {
        var idx : Int = idxx
        if (restore_stack.size <= 0)
            println("Error 200")
        else {
            var last = restore_stack[restore_stack.size-1]
            var last_idx = std_idx.get(last)
            var inserted = 0
            for (std: Int in 0 until students.size) {
                if (std_idx[students[std]]!! > last_idx!!) {
                    students.add(std, last)
                    if (std <= idx)
                        idx += 1
                    restore_stack.removeAt(restore_stack.size-1)
                    inserted = 1
                    break
                }
            }
            if (inserted == 0) {
                restore_stack.removeAt(restore_stack.size-1)
                students.add(last)
            }
        }
        return idx
    }
    
    fun lists(students : MutableList<String>){
        for (std in students)
            println(std)
    }
    
    
    //fun main() {
        var studentss = readLine()!! //.toString()
        studentss = studentss.replace("[","")
        studentss = studentss.replace("]","")
        //studentss = studentss.toString()
        var students = studentss.split(",")
        students = students.toMutableList()
    
        var std_idx = mutableMapOf<String, Int>()
        for (std:Int in 0..students.size-1) {
            students[std] = students[std].replace("\"", "")
            //students[std] = students[std].toInt()
            std_idx[students[std]] = std
        }
    
        var idx : Int = 0
        var user = readLine()
        var restore_stack : MutableList<String> = mutableListOf()
        var range = IntRange(0,6)
    
        while (user != "q") {
            if (user == "delete")
                idx = delete(idx, restore_stack, students)
            else if (user == "restore")
                idx = restore(idx, std_idx, restore_stack, students)
            else if (user == "list")
                lists(students)
            else if (user?.slice(range) == "move -u")
                idx = move_up(idx, user)
            else if (user?.slice(range) == "move -d")
                idx = move_down(idx, user, students)
            user = readLine()
        }
    }