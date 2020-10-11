package me.lasta.studyfaaskotlin2.entrypoint.sample

import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import me.lasta.studyfaaskotlin2.awslambda.LambdaCustomRuntime
import me.lasta.studyfaaskotlin2.monitor.Sentry

private const val URL = "https://jsonplaceholder.typicode.com/posts/1"

@KtorExperimentalAPI
fun main() {
    Sentry.init()
    runBlocking {
        println("Hello main2") // debug
        val runtime = LambdaCustomRuntime()
        runtime.run(URL)
    }
    Sentry.close()
}
