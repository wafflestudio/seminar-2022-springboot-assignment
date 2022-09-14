package specialClasses.objectKeyword

import java.util.Random

class LuckDispatcher {
    fun getNumber() {
        var objRandom = Random()
        println(objRandom.nextInt(90))
    }
}

fun rentPrice(standardDays: Int, festivityDays: Int, specialDays: Int): Unit {

    val dayRates = object { // java의 익명클래스와 유사한 개념
        var standard: Int = 30 * standardDays
        var festivity: Int = 50 * festivityDays
        var special: Int = 100 * specialDays
    }

    val total = dayRates.standard + dayRates.festivity + dayRates.special

    print("Total price: $$total")

}

object DoAuth { // 딱 하나의 객체만 존재함. 싱글턴 패턴
    fun takeParams(username: String, password: String) {
        println("input Auth parameters = $username:$password")
    }
}

class BigBen {
    companion object Bonger { // java의 static method와 유사
        fun getBongs(nTimes: Int) {
            for (i in 1 .. nTimes) {
                print("BONG ")
            }
        }
    }
}

fun main() {
    val d1 = LuckDispatcher()
    val d2 = LuckDispatcher()

    d1.getNumber()
    d2.getNumber()

    println()

    rentPrice(10, 2, 1)

    println()

    DoAuth.takeParams("foo", "qwerty")
    // var doAuth = DoAuth() : 컴파일 에러. object로 선언되었으므로 객체를 만드는 행위가 불가능.

    println()

    BigBen.getBongs(12)
}