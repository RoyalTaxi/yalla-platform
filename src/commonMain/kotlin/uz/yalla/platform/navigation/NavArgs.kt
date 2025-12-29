package uz.yalla.platform.navigation

enum class NavArgType {
    String,
    Int,
    Long,
    Float,
    Bool
}

data class NavArgSpec(
    val name: String,
    val type: NavArgType,
    val nullable: Boolean = false
)

class NavArgs internal constructor(private val values: Map<String, Any?>) {
    fun string(name: String): String? = values[name] as? String
    fun int(name: String): Int? = values[name] as? Int
    fun long(name: String): Long? = values[name] as? Long
    fun float(name: String): Float? = values[name] as? Float
    fun bool(name: String): Boolean? = values[name] as? Boolean

    companion object {
        val Empty = NavArgs(emptyMap())
    }
}
