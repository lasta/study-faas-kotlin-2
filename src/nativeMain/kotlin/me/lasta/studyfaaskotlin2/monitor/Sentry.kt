package me.lasta.studyfaaskotlin2.monitor

import io.ktor.utils.io.core.*
import kotlinx.cinterop.toKString
import libsentry.*
import platform.posix.getenv

/**
 * Usage
 * <code>
 *     sentry_capture_event(
 *         sentry_value_new_message_event(
 *             SENTRY_LEVEL_WARNING, // level
 *             "custom-from-kotlin-native", // logger
 *             "It works from Kotlin/Native!" // message
 *         )
 *     )
 * </code>
 * This object might be class, not object.
 */
object Sentry : Closeable {

    fun init() {
        val options = requireNotNull(sentry_options_new())
        sentry_options_set_dsn(options, requireNotNull(getenv("SENTRY_DSN")).toKString())
        println("SENTRY_DSN: ${requireNotNull(getenv("SENTRY_DSN")).toKString()}") // debug
        sentry_init(options)
        println("sentry has initialized.") // debug
    }

    fun reportInfo(message: String /* logger: Logger*/) {
        sentry_capture_event(
            sentry_value_new_message_event(
                SENTRY_LEVEL_WARNING,
                "custom-from-kotlin-native", // TODO: logger
                message
            )
        )
        println("sentry has reported information.") // debug
    }

    override fun close() {
        // make sure everything flushes
        sentry_shutdown()
        println("sentry has shutdown.") // debug
    }
}
