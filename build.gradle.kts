val ktor_version: String by project

plugins {
    kotlin("multiplatform") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}
group = "me.lasta"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

kotlin {

    // NOTE: The AWS Lambda runtime is on Amazon Linux/2
    val nativeTarget = when (System.getProperty("os.name")) {
        "Mac OS X" -> macosX64("native")
        "Linux" -> linuxX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        compilations.all {
            kotlinOptions.verbose = true
        }
        compilations.getByName("main") {
            val libsentry by cinterops.creating {
                defFile = File(projectDir, "src/nativeMain/interop/libsentry.def")
                includeDirs("/root/sentry/build/include")
                linkerOpts(
                    "-L/root/sentry/build/build",
                    "-L/usr/local/lib64"
                )
            }
        }
        binaries {
//            executable("hello") {
//                baseName = "main"
//                entryPoint = "me.lasta.studyfaaskotlin2.entrypoint.sample.main"
//            }
            executable("sentry-test") {
                baseName = "main"
                entryPoint = "me.lasta.studyfaaskotlin2.entrypoint.sentry"
            }
            // FIXME: Detect entry points automatically
//            entrypoint.ENTRY_POINTS.forEach { entryPoint ->
//                executable(entryPoint.packageName) {
//                    this.entryPoint = entryPoint.entryFunction
//                    runTask?.args(entryPoint.args)
//                }
//            }
        }
    }

    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
        }
        @kotlin.Suppress("UNUSED_VARIABLE")
        val nativeMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-curl:$ktor_version")
                implementation("io.ktor:ktor-client-cio:$ktor_version")
                implementation("io.ktor:ktor-network-tls:$ktor_version")
                implementation("io.ktor:ktor-client-json:$ktor_version")
                implementation("io.ktor:ktor-client-serialization:$ktor_version")
            }
        }

        @kotlin.Suppress("UNUSED_VARIABLE")
        val nativeTest by getting {
            dependencies {
                implementation("io.ktor:ktor-client-mock:$ktor_version")
            }
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "6.6.1"
        distributionType = Wrapper.DistributionType.ALL
    }
}

