package me.lasta.studyfaaskotlin2.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.curl.Curl
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

val httpClient = HttpClient(Curl) {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}
