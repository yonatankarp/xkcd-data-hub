plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.pitest) apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "com.xkcddatahub"
    version = "0.0.1"

    apply(plugin = "info.solidsoft.pitest")

    afterEvaluate {
        plugins.withId("info.solidsoft.pitest") {
            configure<info.solidsoft.gradle.pitest.PitestPluginExtension> {
                targetClasses.set(listOf("com.xkcddatahub.*"))
                threads.set(Runtime.getRuntime().availableProcessors())
                outputFormats.set(listOf("XML", "HTML"))
                timestampedReports.set(false)
                excludedClasses = listOf(
                    "com.xkcddatahub.*.di.*"
                )
            }
        }
    }
}
