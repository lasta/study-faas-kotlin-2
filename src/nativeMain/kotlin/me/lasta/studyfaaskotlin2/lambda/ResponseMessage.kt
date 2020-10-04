package me.lasta.studyfaaskotlin2.lambda

import kotlinx.serialization.Serializable

@Serializable
data class ResponseMessage(
    val statusCode: Int = 200,
    val body: String
)

