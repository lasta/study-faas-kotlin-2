package me.lasta.studyfaaskotlin2.entrypoint.serialization

//import kotlinx.serialization.Serializable
//import kotlinx.serialization.decodeFromString
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json
//
///**
// * copied from https://github.com/Kotlin/kotlinx.serialization#introduction-and-references
// */
//@Serializable
//data class Project(val name: String, val language: String)
//
//fun main() {
//    // Serializing objects
//    val data = Project("kotlinx.serialization", "Kotlin")
//    val string = Json.encodeToString(data)
//    println(string) // {"name":"kotlinx.serialization","language":"Kotlin"}
//    // Deserializing back into objects
//    val obj = Json.decodeFromString<Project>(string)
//    println(obj) // Project(name=kotlinx.serialization, language=Kotlin)
//}
fun main() {
    println("entrypoint/serialization/main")
}