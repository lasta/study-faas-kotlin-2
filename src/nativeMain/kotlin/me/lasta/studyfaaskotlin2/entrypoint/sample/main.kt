package me.lasta.studyfaaskotlin2.entrypoint.sample

import io.ktor.client.*
import io.ktor.client.engine.curl.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.lasta.studyfaaskotlin2.awslambda.LambdaCustomRuntime
import me.lasta.studyfaaskotlin2.awslambda.LambdaCustomRuntimeEnv
import me.lasta.studyfaaskotlin2.entity.UserArticle
import platform.posix.getenv

private const val URL = "https://jsonplaceholder.typicode.com/posts/1"

@KtorExperimentalAPI
fun main() {
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

                // client must be initialize each to use
                val userArticle: UserArticle = HttpClient(Curl) {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                }.use { client ->
                    try {
                        println("request: $URL")
                        client.get(URL)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        lambdaCustomRuntime.sendInvocationError(lambdaEnv, e)
                        null
                    }
                } ?: continue
                println(userArticle)
                println(Json.encodeToString(userArticle))
                lambdaCustomRuntime.sendResponse(lambdaEnv, userArticle)
            }
        } catch (e: Exception) {
            // Initialization Error
            println("Initialization Error")
            e.printStackTrace()
            lambdaCustomRuntime.sendInitializeError(lambdaEnv, e)
        }
    }
}
