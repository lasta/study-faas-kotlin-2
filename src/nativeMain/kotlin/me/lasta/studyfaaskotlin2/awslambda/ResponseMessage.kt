package me.lasta.studyfaaskotlin2.awslambda

import kotlinx.serialization.Serializable

@Serializable
data class ResponseMessage<T>(
    val statusCode: Int = 200,
    val body: T
)

