package com.wafflestudio.seminar.test

/**
 * TODO 
 *   3번을 코틀린으로 다시 한번 풀어봐요.
 *   객체를 통한 구조화를 시도해보면 좋아요 :)
 */
fun main() {
    // 여기를 채워 주세요!
    var firstInput=readLine();
    if (firstInput != null) {
        val sliceNum=firstInput.length-3
        firstInput=firstInput.slice(IntRange(2,sliceNum))
    }
    println(firstInput)
    val mainArray= firstInput?.split("\",\"")?.toMutableList()
    println(mainArray)
    var currentSelect=0;
    while (true){
        val command= readLine().toString();
        val commandArray=command.split(" ")
        if(commandArray[0]=="move") {
            /*
            move -u <숫자>
            현재 선택된 행에서 입력된 숫자만큼 위에 있는 행을 선택합니다.
            만약 이동하려는 행이 0보다 작다면 Error 100 을 출력합니다.
            move -d <숫자>
            현재 선택된 행에서 입력된 숫자만큼 아래에 있는 행을 선택합니다.
            만약 이동하려는 행이 n 보다 크다면 Error 100 을 출력합니다.
             */
            if (commandArray[1] == "-u") {
                if (currentSelect - commandArray[2].toInt() < 0) {
                    println("Error 100")
                    continue
                }
                currentSelect -= commandArray[2].toInt()
            } else if (commandArray[1] == "-d") {
                if (mainArray != null) {
                    if (currentSelect + commandArray[2].toInt() > mainArray.size - 1) {
                        println("Error 100")
                        continue
                    }
                    currentSelect += commandArray[2].toInt()
                }
                println(currentSelect)
            }
        }
        else if(commandArray[0]=="delete"){
                mainArray?.removeAt(currentSelect)
                println(mainArray)
        }
        else if(commandArray[0]=="restore"){
            print("ok")
        }
        else if(commandArray[0]=="list"){
            println(mainArray)
        }
        else if(commandArray[0]=="q") {
            break
        }
    }
}
