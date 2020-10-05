package me.lasta.studyfaaskotlin2.awslambda

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LambdaRuntimeRequestBody(
        @SerialName("queryStringParameters")
        val parameters: Map<String, String>
)
