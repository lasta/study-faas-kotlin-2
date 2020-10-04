package me.lasta.studyfaaskotlin2.entrypoint.withbootstrap

import io.ktor.client.*
import io.ktor.client.engine.curl.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import me.lasta.studyfaaskotlin2.lambda.CustomRuntime
import me.lasta.studyfaaskotlin2.lambda.RuntimeEnv

@KtorExperimentalAPI
fun main() = runBlocking {
    lateinit var lambdaEnv: RuntimeEnv
    val httpClient = HttpClient(Curl)
    val customRuntime = CustomRuntime(client = httpClient)

    try {
        bootstrapLoop@ while (true) {
            // preprocessing each request
            lambdaEnv = customRuntime.initialize()

            // business logic
            val userArticle: String = HttpClient(Curl).use { client ->
                try {
                    // https://jsonplaceholder.typicode.com/
                    client.get("https://jsonplaceholder.typicode.com/posts/1")
                } catch (e: Exception) {
                    // Application error each request
                    println("Application Error")
                    e.printStackTrace()
                    customRuntime.sendInvocationError(lambdaEnv, e)
                    null
                }
            } ?: continue
            // postprocessing each request
            customRuntime.sendResponse(lambdaEnv, userArticle)
        }
    } catch (e: Exception) {
        // Initialization error
        print("Initialization Error")
        e.printStackTrace()
        customRuntime.sendInitializeError(lambdaEnv, e)
    }
}

