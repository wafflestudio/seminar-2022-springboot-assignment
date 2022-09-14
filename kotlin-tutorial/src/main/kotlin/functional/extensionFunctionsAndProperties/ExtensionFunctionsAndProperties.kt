package functional.extensionFunctionsAndProperties

data class Item(val name: String, val price: Float)

data class Order(val items: Collection<Item>)

// maxByOrNull : 가장 큰 값, 비어있으면 null 반환
// ?. : null이 아니면 함수(.price, .name) 호출, null이면 무시
// ?: : null이면 ?: 뒤의 값(0F, "NO_PRODUCTS")이 들어감
fun Order.maxPricedItemValue(): Float = this.items.maxByOrNull { it.price }?.price ?: 0F
fun Order.maxPricedItemName() = this.items.maxByOrNull { it.price }?.name ?: "NO_PRODUCTS"

val Order.cammaDelimitedItemNames: String
    get() = items.map { it.name }.joinToString()

fun main() {
    val order = Order(listOf(Item("Bread", 25.0F), Item("Wine", 29.0F), Item("Water", 12.0F)))

    println("Max priced item name: ${order.maxPricedItemName()}")
    println("Max priced item value: ${order.maxPricedItemValue()}")
    println("Items: ${order.cammaDelimitedItemNames}")

    // null 자체에서 함수 호출도 가능함!
    fun <T> T?.nullSafeToString() = this?.toString() ?: "NULL"
    println(null.nullSafeToString())
    println("Kotlin".nullSafeToString())
}