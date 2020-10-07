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
    sentry_capture_event(
        sentry_value_new_message_event(
            SENTRY_LEVEL_INFO, // level
            "custom", // logger
            "It works from Kotlin/Native!" // message
        )
    )

    // make sure everything flushes
    sentry_shutdown()
}
