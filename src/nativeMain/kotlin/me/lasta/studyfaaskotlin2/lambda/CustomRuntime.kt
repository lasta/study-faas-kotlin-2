package me.lasta.studyfaaskotlin2.lambda

import io.ktor.client.*
import io.ktor.client.engine.curl.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.cinterop.toKString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.posix.getenv

// TODO: get client with function variable
class CustomRuntime(
    private val api: String = requireNotNull(getenv("AWS_LAMBDA_RUNTIME_API")).toKString(),
    private val client: HttpClient = HttpClient(Curl)
) {

    private val baseUrl: String
        get() = "http://$api/2018-06-01/runtime"

    private suspend fun doPost(action: String, body: Any? = null): HttpResponse = client.use {
        it.post {
            url("$baseUrl/$action")
            if (body != null) {
                this.body = Json.encodeToString(body)
            }
        }
    }

    @KtorExperimentalAPI
    suspend fun initialize(): RuntimeEnv = try {
        RuntimeEnv(doPost(action = "/invocation/next"))
    } catch (e: Exception) {
        e.printStackTrace()
        TODO("do something from initialize")
    }

    suspend fun sendInvocationError(lambdaEnv: RuntimeEnv, error: Exception): HttpResponse = try {
        doPost(
            action = "/invocation/${lambdaEnv.requestId}/error",
            body = mapOf(
                "errorMessage" to error.toString(),
                "errorType" to "InvocationError"
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
        TODO("do something from send invocation error")
    }

    suspend fun sendInitializeError(lambdaEnv: RuntimeEnv, error: Exception): HttpResponse = try {
        doPost(
            action = "/init/error",
            body = mapOf(
                "errorMessage" to error.toString(),
                "errorType" to "InvocationError"
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
        TODO("do something from send initialize error")
    }

    suspend fun sendResponse(lambdaEnv: RuntimeEnv, response: String): HttpResponse = try {
        doPost(
            action = "/${lambdaEnv.requestId}/response",
            body = ResponseMessage(body = response)
        )
    } catch (e: Exception) {
        e.printStackTrace()
        TODO("do something from send response")
    }
}
