package me.lasta.studyfaaskotlin2.awslambda

import io.ktor.client.*
import io.ktor.client.engine.curl.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.content.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.cinterop.toKString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.lasta.studyfaaskotlin2.monitor.Sentry
import platform.posix.getenv

class LambdaCustomRuntime {
    val httpClient
        get() = HttpClient(Curl) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }

    val lambdaRuntimeApi: String
        get() = requireNotNull(getenv("AWS_LAMBDA_RUNTIME_API")).toKString()

    val baseUrl: String
        get() = "http://$lambdaRuntimeApi/2018-06-01/runtime"

    @KtorExperimentalAPI
    suspend inline fun <reified T> run(block: (LambdaCustomRuntime) -> T) {
        lateinit var lambdaEnv: LambdaCustomRuntimeEnv
        try {
            while (true) {
                lambdaEnv = initialize()

                val response = try {
                    block(this)
                } catch (e: Exception) {
                    e.printStackTrace()
                    sendInvocationError(lambdaEnv, e)
                    null
                } ?: continue

                Sentry.reportInfo("Succeeded to send response")
                sendResponse(lambdaEnv, response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            sendInitializeError(lambdaEnv, e)
        }
    }

    @KtorExperimentalAPI
    suspend inline fun initialize(): LambdaCustomRuntimeEnv = httpClient.use { client ->
        try {
            LambdaCustomRuntimeEnv(client.get("$baseUrl/invocation/next"))
        } catch (e: Exception) {
            TODO("do something from initialize")
        }
    }

    @KtorExperimentalAPI
    suspend inline fun sendInvocationError(
        lambdaEnv: LambdaCustomRuntimeEnv,
        error: Exception
    ): HttpResponse = httpClient.use { client ->
        try {
            client.post {
                url("http://$lambdaRuntimeApi/2018-06-01/runtime/invocation/${lambdaEnv.requestId}/error")
                body = TextContent(
                    Json.encodeToString(
                        mapOf(
                            "errorMessage" to error.toString(),
                            "errorType" to "InvocationError"
                        )
                    ),
                    contentType = ContentType.Application.Json
                )
            }
        } catch (e: Exception) {
            TODO("do something from send invocation error")
        }
    }

    @KtorExperimentalAPI
    suspend inline fun sendInitializeError(
        lambdaEnv: LambdaCustomRuntimeEnv,
        error: Exception
    ): HttpResponse = httpClient.use { client ->
        try {
            client.post {
                url("http://$lambdaRuntimeApi/2018-06-01/runtime/init/error")
                body = TextContent(
                    Json.encodeToString(
                        mapOf(
                            "errorMessage" to error.toString(),
                            "errorType" to "InvocationError"
                        )
                    ),
                    contentType = ContentType.Application.Json
                )
            }
        } catch (e: Exception) {
            TODO("do something from send initialize error")
        }
    }

    @KtorExperimentalAPI
    suspend inline fun <reified T> sendResponse(
        lambdaEnv: LambdaCustomRuntimeEnv,
        response: T
    ): HttpResponse = httpClient.use { client ->
        try {
            client.post {
                url("http://$lambdaRuntimeApi/2018-06-01/runtime/invocation/${lambdaEnv.requestId}/response")
                // FIXME: client serializes to Json by default content-type automatically but it raises ArrayIndexOutOfBoundsException
                body = TextContent(
                    Json.encodeToString(ResponseMessage(body = Json.encodeToString(response))),
                    contentType = ContentType.Application.Json
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            TODO("do something from send response")
        }
    }
}
