package com.wafflestudio.seminar.test

import java.util.*

/**
 * TODO
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
fun main() {
    val student = Student(saveData())
    while(true){
        var line :String ? = readLine();
        if(line==null){return}
        if(line.length==1){return}
        if(line.contains("move -u")){
            line=line.trim()
            val len=line.length
            var num=line.substring(8,len).toInt()
            student.move_up(num)
            continue
        }
        if(line.contains("move -d")){
            line=line.trim()
            val len=line.length
            var num=line.substring(8,len).toInt()
            student.move_down(num)
            continue
        }
        if(line.contains("delete")){
            student.delete()
            continue
        }
        if(line.contains("list")){
            student.list()
            continue
        }
        if(line.contains("restore")){
            student.restore()
            continue
        }
    }

}
fun saveData() : MutableList<String>{
    var input= readLine()!!
    input=input.trim()
    input=input.substring(1,input.length-1)
    var data = input.split(",").toMutableList()
    var idx=0
    data.forEach{
        data[idx++]=it.substring(1,it.length-1)
    }
    return data
//    println(data[data.size-1])
//    println(data[0])
}
class Student(private val data: MutableList<String>, private var point: Int =0, private var restored: Stack<Int> = Stack<Int>() ){
    private val dataSize=data.size
    private var isTransfer : Array<Boolean> = Array(dataSize){i->false}

    fun list() {
        for(i: Int in 0..dataSize-1){
            if(!isTransfer[i]){
                print(data[i]+"\n")
            }
        }
        return
    }

    fun delete() {
        isTransfer[point]=true
        restored.add(point)

        for(i :Int in point+1..dataSize-1){
            if(!isTransfer[i]){
                point=i

                return
            }
        }
        for(i:Int in (point - 1) downTo 0){
            if(!isTransfer[i]){
                point=i
                return
            }
        }

    }

    fun move_down(numericValue: Int) {
        var c=0
        var tmp_point=point
        while(tmp_point<=dataSize-1){
            if(c==numericValue){
                point=tmp_point
                //print("move down "+ numericValue + " point : "+point+"\n")
                return}
            tmp_point++
            if(tmp_point>dataSize-1){break}
            if(!isTransfer[tmp_point]){c++}
        }
        print("Error 100\n")
        return
    }

    fun move_up(numericValue: Int) {
        var c=0
        var tmp_point=point
        while(tmp_point>=0){
            if(c==numericValue){
                point=tmp_point
                //print("move up "+ numericValue + " point : "+point+"\n")
                return}
            tmp_point--
            if(tmp_point<0){break}
            if(!isTransfer[tmp_point]){c++}
        }
        print("Error 100\n")
        return
    }

    fun restore() {
        if(restored.empty()){
            print("Error 200\n")
            return
        }
        isTransfer[restored.pop()]=false
    }
}