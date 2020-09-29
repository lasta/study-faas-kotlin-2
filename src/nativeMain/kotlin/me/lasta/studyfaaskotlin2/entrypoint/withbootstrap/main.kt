package me.lasta.studyfaaskotlin2.entrypoint.withbootstrap

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.posix.getenv

@KtorExperimentalAPI
fun main() = runBlocking {
    println("hello withbootstrap")
    lateinit var lambdaEnv: LambdaCustomRuntimeEnv

    try {
        // initialize
        val lambdaRuntimeApi = requireNotNull(getenv("AWS_LAMBDA_RUNTIME_API")).toKString()

        bootstrapLoop@ while (true) {
            // preprocessing each request
            lambdaEnv = LambdaCustomRuntime.initialize()

            // business logic
            val userArticles: List<UserArticle> = HttpClient(CIO).use { client ->
                try {
                    // https://jsonplaceholder.typicode.com/
                    client.get("https://jsonplaceholder.typicode.com/posts")
                } catch (e: Exception) {
                    // Application error each request
                    println("Application Error")
                    e.printStackTrace()
                    LambdaCustomRuntime.sendInvocationError(lambdaEnv, e)
                    null
                }
            } ?: continue
            // postprocessing each request
            LambdaCustomRuntime.sendResponse(lambdaEnv, userArticles)
        }
    } catch (e: Exception) {
        // Initialization error
        print("Initialization Error")
        e.printStackTrace()
        LambdaCustomRuntime.sendInitializeError(lambdaEnv, e)
    }
}

@Serializable
data class UserArticle(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

object LambdaCustomRuntime {
    val lambdaRuntimeApi = requireNotNull(getenv("AWS_LAMBDA_RUNTIME_API")).toKString()

    @KtorExperimentalAPI
    suspend fun initialize(): LambdaCustomRuntimeEnv = HttpClient(CIO).use { client ->
        try {
            LambdaCustomRuntimeEnv(client.get("http://$lambdaRuntimeApi/2018-06-01/runtime/invocation/next"))
        } catch (e: Exception) {
            TODO("do something")
        }
    }

    @KtorExperimentalAPI
    suspend fun sendInvocationError(lambdaEnv: LambdaCustomRuntimeEnv, error: Exception) {
        HttpClient(CIO).use { client ->
            val proc: HttpResponse = try {
                client.post {
                    url("http://$lambdaRuntimeApi/2018-06-01/runtime/invocation/${lambdaEnv.requestId}/error")
                    body = Json.encodeToString(
                        mapOf(
                            "errorMessage" to error.toString(),
                            "errorType" to "InvocationError"
                        )
                    )
                }
            } catch (e: Exception) {
                TODO("do something")
            }

            println(proc.toString())
        }
    }

    @KtorExperimentalAPI
    suspend fun sendInitializeError(lambdaEnv: LambdaCustomRuntimeEnv, error: Exception) {
        HttpClient(CIO).use { client ->
            val proc: HttpResponse = try {
                client.post {
                    url("http://$lambdaRuntimeApi/2018-06-01/runtime/init/error")
                    body = Json.encodeToString(
                        mapOf(
                            "errorMessage" to error.toString(),
                            "errorType" to "InvocationError"
                        )
                    )
                }
            } catch (e: Exception) {
                TODO("do something")
            }

            println(proc.toString())
        }
    }

    @KtorExperimentalAPI
    suspend inline fun <reified T> sendResponse(lambdaEnv: LambdaCustomRuntimeEnv, response: T) {
        HttpClient(CIO).use { client ->
            val proc: HttpResponse = try {
                client.post {
                    url("http://$lambdaRuntimeApi/2018-06-01/runtime/invocation/${lambdaEnv.requestId}/response")
                    body = Json.encodeToString(
                        mapOf(
                            "statusCode" to 200,
                            "body" to Json.encodeToString(response)
                        )
                    )
                }
            } catch (e: Exception) {
                TODO("do something")
            }
            println(proc.toString())
        }
    }
}

class LambdaCustomRuntimeEnv(
    private val response: HttpResponse
) {
    private val body: LambdaRuntimeRequestBody
        // TODO change body data class
        get() = Json.decodeFromString(response.toString())

    val requestId: String
        get() = requireNotNull(
            response.headers["lambda-runtime-aws-request-id"],
            lazyMessage = { "\"lambda-runtime-aws-request-id\" must not be null" }
        )

    // FIXME: define request value in LambdaRuntimeRequestBody and delete this function
    fun getRequestParameter(key: String) = body.parameters[key]
}

@Serializable
data class LambdaRuntimeRequestBody(
    @SerialName("queryStringParameters")
    val parameters: Map<String, String>
)
