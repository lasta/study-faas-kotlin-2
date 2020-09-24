plugins {
    kotlin("multiplatform") version "1.4.0"
}
group = "me.lasta"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
//    val nativeTarget = when {
//        hostOs == "Mac OS X" -> macosX64("native")
//        hostOs == "Linux" -> linuxX64("native")
//        isMingwX64 -> mingwX64("native")
//        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
//    }

    val nativeTarget = linuxX64("native")

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    sourceSets {
        @kotlin.Suppress("UNUSED_VARIABLE")
        val nativeMain by getting
        @kotlin.Suppress("UNUSED_VARIABLE")
        val nativeTest by getting
    }
}