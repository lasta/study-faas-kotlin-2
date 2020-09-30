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

    // The AWS Lambda runtime is on Amazon Linux/2
    val nativeTarget = linuxX64("native")

    nativeTarget.apply {
        compilations.all {
            kotlinOptions.verbose = true
        }
        binaries {
//            entrypoint.ENTRY_POINTS.forEach { entryPoint ->
//                executable(entryPoint.packageName) {
//                    this.entryPoint = entryPoint.entryFunction
//                    runTask?.args(entryPoint.args)
//                }
//            }
            executable("me.lasta.studyfaaskotlin2.entrypoint.withbootstrap") {
                baseName = "bootstrap"
                entryPoint = "me.lasta.studyfaaskotlin2.entrypoint.withbootstrap.main"
            }
        }
    }

    sourceSets {
        // TODO: define in buildSrc
        val ktor_version = "1.4.1"

        @kotlin.Suppress("UNUSED_VARIABLE")
        val nativeMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-cio:$ktor_version")
                implementation("io.ktor:ktor-network-tls:$ktor_version")
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

tasks.register<Copy>("putBootstrapForSAMLocal") {
    from(file("$buildDir/bin/native/me.lasta.studyfaaskotlin2.entrypoint.withbootstrapReleaseExecutable/bootstrap.kexe"))
    into(file("sam/bootstrap"))
}

tasks.register<Exec>("startSAMLocal") {
    task("putBootstrapForSAMLocal")
    commandLine("sam", "local", "start-api", "-t", "sam/template.yaml")
}
