package me.lasta.studyfaaskotlin2.lambda

import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class RuntimeEnv(
    private val response: HttpResponse
) {
    private val body: RuntimeRequestBody
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

