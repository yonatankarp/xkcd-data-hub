import com.diffplug.gradle.spotless.SpotlessExtension
import info.solidsoft.gradle.pitest.PitestPluginExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.pitest) apply false
    alias(libs.plugins.spotless) apply true
}

repositories { mavenCentral() }

pluginManager.withPlugin("com.diffplug.spotless") {
    extensions.configure<SpotlessExtension>("spotless") {
        kotlin {
            ktlint("1.5.0")
            trimTrailingWhitespace()
            target("**/src/**/*.kt")
        }
        kotlinGradle {
            ktlint("1.5.0")
            trimTrailingWhitespace()
            target("**/*.kts")
        }
        flexmark {
            flexmark()
            trimTrailingWhitespace()
            target("**/*.md")
        }
    }
}

subprojects {
    group = "com.xkcddatahub"
    version = "0.0.1"

    repositories { mavenCentral() }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions.jvmTarget = JvmTarget.JVM_21
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    pluginManager.withPlugin("info.solidsoft.pitest") {
        extensions.configure<PitestPluginExtension>("pitest") {
            targetClasses.set(listOf("com.xkcddatahub.*"))
            threads.set(Runtime.getRuntime().availableProcessors())
            outputFormats.set(listOf("XML", "HTML"))
            junit5PluginVersion = "1.2.1"
            timestampedReports.set(false)
            excludedClasses = listOf("com.xkcddatahub.*.di.*")
        }
    }
}

tasks.register("copyGitHooks", Copy::class) {
    from(".github/githooks")
    into(".git/hooks")
}
