package me.lasta.studyfaaskotlin2.entrypoint.httpclient

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking

@KtorExperimentalAPI
fun main() = runBlocking {
    HttpClient(CIO) {
        install(HttpTimeout) {
        }
        install(UserAgent) {
            agent = "some user agent"
        }
    }.use { client ->
        val message: String = client.get("http://example.com")
        println("Hello, Ktor CIO client")
        println(message)
    }
}
