plugins {
    kotlin("multiplatform") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}
group = "me.lasta"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

kotlin {

    // TODO: cross-compile for platforms at once
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }


    nativeTarget.apply {
        compilations.all {
            kotlinOptions.verbose = true
        }
        binaries {
            entrypoint.ENTRY_POINTS.forEach { entryPoint ->
                executable(entryPoint.packageName) {
                    this.entryPoint = entryPoint.entryFunction
                    runTask?.args(entryPoint.args)
                }
            }
        }
    }

    sourceSets {
        @kotlin.Suppress("UNUSED_VARIABLE")
        val nativeMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
            }
        }

        @kotlin.Suppress("UNUSED_VARIABLE")
        val nativeTest by getting
    }
}

tasks {
    wrapper {
        gradleVersion = "6.6.1"
        distributionType = Wrapper.DistributionType.ALL
    }
}