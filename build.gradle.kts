plugins {
    kotlin("multiplatform") version "1.4.0"
}
group = "me.lasta"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
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

    // TODO: extract to buildSrc
    val entryPointPackages = listOf(
        "",
        "me.lasta.studyfaaskotlin2.entrypoint"
    )

    nativeTarget.apply {
        binaries {
            entryPointPackages.forEach { entryPointPackage ->
                executable(entryPointPackage) {
                    this.entryPoint = "$entryPointPackage.main"
                    // pass args
//                    runTask?.args(entryPoint.args)
                }
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

tasks {
    wrapper {
        gradleVersion = "6.6"
        distributionType = Wrapper.DistributionType.ALL
    }
}