package introduction.nullSafety

fun main() {

    // Null Safety
    var neverNull: String = "This can't be null"
    // neverNull = null : 컴파일 에러. null이 될 수 없음.

    var nullable: String? = "You can keep a null here"
    nullable = null // 타입명 뒤에 ?가 붙었으므로 null이 될 수 있음

    var inferredNonNull = "The complier assumes non-null"
    // inferredNonNull = null : 컴파일 에러. 명시되어있지 않으면 컴파일러는 non-null로 가정

    fun strLength(notNull: String): Int {
        return notNull.length
    }

    println(strLength(neverNull))
    // strLength(nullable) : 컴파일 에러. nullable이기 때문

    // Working with Nulls
    fun describeString(maybeString: String?): String {
        if (maybeString != null && maybeString.length > 0) {
            return "String of length ${maybeString.length}"
        } else {
            return "Empty or null string"
        }
    }
    println(describeString(neverNull))
    println(describeString(nullable))
}
