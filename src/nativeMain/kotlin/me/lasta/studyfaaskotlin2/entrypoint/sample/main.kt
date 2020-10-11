package me.lasta.studyfaaskotlin2.entrypoint.sample

import io.ktor.client.*
import io.ktor.client.engine.curl.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import me.lasta.studyfaaskotlin2.awslambda.LambdaCustomRuntime
import me.lasta.studyfaaskotlin2.awslambda.LambdaCustomRuntimeEnv
import me.lasta.studyfaaskotlin2.entity.UserArticle
import me.lasta.studyfaaskotlin2.monitor.Sentry

private const val URL = "https://jsonplaceholder.typicode.com/posts/1"

@KtorExperimentalAPI
fun main() {
    Sentry.init()
    runBlocking {
        LambdaCustomRuntime().exec(fetchUserArticle)
    }
    Sentry.close()
}

val fetchUserArticle: (LambdaCustomRuntimeEnv) -> UserArticle = { _ ->
    runBlocking {
        HttpClient(Curl) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }.use { client ->
            println("request: $URL")
            client.get(URL)
        }
    }
}
