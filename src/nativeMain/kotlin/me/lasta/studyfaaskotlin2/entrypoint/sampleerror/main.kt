package me.lasta.studyfaaskotlin2.entrypoint.sampleerror

import io.ktor.util.*
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import me.lasta.studyfaaskotlin2.awslambda.LambdaCustomRuntime
import me.lasta.studyfaaskotlin2.awslambda.LambdaCustomRuntimeEnv
import me.lasta.studyfaaskotlin2.monitor.Sentry
import platform.posix.getenv

@KtorExperimentalAPI
fun main() {
    Sentry.init()
    runBlocking {
        println("Hello main2") // debug
        val lambdaRuntimeApi = requireNotNull(getenv("AWS_LAMBDA_RUNTIME_API")).toKString()

        val lambdaCustomRuntime = LambdaCustomRuntime(
            lambdaRuntimeApi = lambdaRuntimeApi
        )

        lateinit var lambdaEnv: LambdaCustomRuntimeEnv
        try {
            while (true) {
                lambdaEnv = lambdaCustomRuntime.initialize()
                try {
                    throw Exception("Sample exception")
                } catch (e: Exception) {
                    e.printStackTrace()
                    lambdaCustomRuntime.sendInvocationError(lambdaEnv, e)
                }
            }
        } catch (e: Exception) {
            // Initialization Error
            println("Initialization Error")
            e.printStackTrace()
            lambdaCustomRuntime.sendInitializeError(lambdaEnv, e)
        }
    }
    Sentry.close()
}
