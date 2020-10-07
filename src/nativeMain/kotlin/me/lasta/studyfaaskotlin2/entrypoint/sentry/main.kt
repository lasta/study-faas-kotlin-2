package me.lasta.studyfaaskotlin2.entrypoint.sentry

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString
import libsentry.*
import platform.posix.getenv

/**
 * Sample running with sentry
 */
fun main() {
    val options: CPointer<sentry_options_t> = requireNotNull(sentry_options_new())
    sentry_options_set_dsn(options, requireNotNull(getenv("SENTRY_DSN")).toKString())

    /* do something */
    println("""
    sentry_capture_event(
        sentry_value_new_message_event(
            SENTRY_LEVEL_WARNING, // level
            "custom-from-kotlin-native", // logger
            "It works from Kotlin/Native!" // message
        )
    )
    """.trimIndent())
    sentry_capture_event(
        sentry_value_new_message_event(
            SENTRY_LEVEL_WARNING, // level
            "custom-from-kotlin-native", // logger
            "It works from Kotlin/Native!" // message
        )
    )
    println("done!!")

    // make sure everything flushes
    sentry_shutdown()
}
