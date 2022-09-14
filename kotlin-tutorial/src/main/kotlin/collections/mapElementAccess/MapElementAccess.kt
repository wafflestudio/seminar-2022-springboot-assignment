package collections.mapElementAccess

import java.util.NoSuchElementException

val map = mapOf("key" to 42)

val value1 = map["key"]
val value2 = map["key2"] // key2가 맵의 key 중에 없으므로 null

val value3: Int = map.getValue("key")

val mapWithDefault = map.withDefault { k -> k.length }
val value4 = mapWithDefault.getValue("key2") // key2가 맵의 key 중에 없고 길이가 4이므로 4

fun main() {
    println("map is $map")
    println("value1 is $value1")
    println("value2 is $value2")
    println("value3 is $value3")

    println("mapWithDefault is $mapWithDefault")
    println("value4 is $value4")

    try {
        map.getValue("anotherKey")
    } catch (e: NoSuchElementException) {
        println("Message: $e")
    }
}