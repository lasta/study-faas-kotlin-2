package me.lasta.studyfaaskotlin2.entrypoint.sentry

import io.ktor.utils.io.core.*
import me.lasta.studyfaaskotlin2.monitor.Sentry

/**
 * Sample running with sentry
 */
fun main() {
    Sentry().use { sentry ->

        /* do something */
        println(
            """
    sentry_capture_event(
        sentry_value_new_message_event(
            SENTRY_LEVEL_WARNING, // level
            "custom-from-kotlin-native", // logger
            "It works from Kotlin/Native!" // message
        )
    )
    """.trimIndent()
        )
        sentry.reportInfo(
            "It works from Kotlin/Native!!!!!!!!" // message
        )
        println("done!!")
    }
}
