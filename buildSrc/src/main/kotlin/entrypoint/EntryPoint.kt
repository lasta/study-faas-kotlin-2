package entrypoint

data class EntryPoint(
    val packageName: String,
    val functionName: String = "main",
    val args: Array<String>? = null
) {
    val entryFunction: String
        get() = "$packageName.$functionName"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EntryPoint

        if (packageName != other.packageName) return false
        if (functionName != other.functionName) return false
        if (args != null) {
            if (other.args == null) return false
            if (!args.contentEquals(other.args)) return false
        } else if (other.args != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = packageName.hashCode()
        result = 31 * result + functionName.hashCode()
        result = 31 * result + (args?.contentHashCode() ?: 0)
        return result
    }

}

val ENTRY_POINTS: List<EntryPoint> = listOf(
    // TODO: generate automatically with project structure
    EntryPoint(packageName = ""),
    EntryPoint(packageName = "me.lasta.studyfaaskotlin2.entrypoint")
)