import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.pitest) apply false
    alias(libs.plugins.spotless) apply true
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
    apply(plugin = "com.diffplug.spotless")

    afterEvaluate {
        plugins.withId("com.diffplug.spotless") {
            configure<com.diffplug.gradle.spotless.SpotlessExtension> {
                kotlin {
                    ktlint()
                    target("src/**/*.kt")
                }
                kotlinGradle {
                    ktlint()
                    target("src/**/*.kts")
                }
                flexmark {
                    flexmark()
                    target("src/**/*.md")
                }
            }
        }

        plugins.withId("info.solidsoft.pitest") {
            configure<info.solidsoft.gradle.pitest.PitestPluginExtension> {
                targetClasses.set(listOf("com.xkcddatahub.*"))
                threads.set(Runtime.getRuntime().availableProcessors())
                outputFormats.set(listOf("XML", "HTML"))
                junit5PluginVersion = "1.2.1"
                timestampedReports.set(false)
                excludedClasses = listOf(
                    "com.xkcddatahub.*.di.*"
                )
            }
        }

        tasks.withType(Test::class) {
            useJUnitPlatform()
        }

        tasks.withType(KotlinCompile::class.java) {
            dependsOn("spotlessCheck", ":copyGitHooks")
        }
    }
}

tasks.register("copyGitHooks", Copy::class) {
    from(".github/githooks")
    into(".git/hooks")
}


