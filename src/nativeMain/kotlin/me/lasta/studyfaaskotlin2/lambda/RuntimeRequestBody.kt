package me.lasta.studyfaaskotlin2.lambda

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RuntimeRequestBody(
    @SerialName("queryStringParameters")
    val parameters: Map<String, String>
)
