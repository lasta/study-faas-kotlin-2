package entrypoint

data class EntryPoint(
    val packageName: String,
    val functionName: String = "main",
    val args: Array<String> = emptyArray()
) {
    val entryFunction: String
        get() = "$packageName.$functionName"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EntryPoint

        if (packageName != other.packageName) return false

        return true
    }

    override fun hashCode(): Int {
        return packageName.hashCode()
    }
}

val ENTRY_POINTS: List<EntryPoint> = listOf(
    // TODO: generate automatically with project structure
    EntryPoint(packageName = "me.lasta.studyfaaskotlin2.entrypoint.withbootstrap")
)